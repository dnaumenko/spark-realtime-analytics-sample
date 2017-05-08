package com.github.sparksample.httpapp

import java.util.concurrent.{Callable, TimeUnit}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.google.common.cache.{Cache, CacheBuilder}
import spray.json._

import scala.concurrent.Future
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes

final case class Identity(id: Long)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val identityFormat: RootJsonFormat[Identity] = jsonFormat1(Identity)
}

object SimpleServer extends Directives with JsonSupport {

  private val cache: Cache[String, Dataset[Row]] = CacheBuilder.newBuilder()
    .expireAfterWrite(1, TimeUnit.MINUTES).build[String, Dataset[Row]]()

  def routes(ssc: SparkSession): Route = {
    pathPrefix("api") {
      path("events" / "count") {
        get {
          val df = ssc.read
            .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test_dump", "table" -> "events"))
            .load()
          val count = df.count()

          complete(s"Identities count: $count")
        }
      } ~ path("events" / "retention") {
        val events = cache.get("events", new Callable[Dataset[Row]] {
          override def call(): Dataset[Row] =  {
            val result = ssc.read
              .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test_dump", "table" -> "events"))
              .load()
              .where("event_id == 4")

            result.cache() // mark cached in Spark internals to allow re-use
          }
        })
        println("Number of events partitions: " + events.rdd.getNumPartitions)

        val devices = cache.get("devices", new Callable[Dataset[Row]] {
          override def call(): Dataset[Row] =  {
            val result = ssc.read
              .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test_dump", "table" -> "devices"))
              .load()

            result.cache() // mark cached in Spark internals to allow re-use
          }
        })
        println("Number of devices partitions: " + devices.rdd.getNumPartitions)

        val join = events
          .join(devices, Seq("device_id"), "left")
          .withColumn("install_date", col("received_at").cast(DataTypes.DateType))
          .withColumn("event_date", col("event_at").cast(DataTypes.DateType))
          .withColumn("d1_retention", when(col("event_date") === date_add(col("install_date"), 1), 1).otherwise(0))
          .withColumn("d7_retention", when(col("event_date") === date_add(col("install_date"), 7), 1).otherwise(0))
          .withColumn("d15_retention", when(col("event_date") === date_add(col("install_date"), 15), 1).otherwise(0))
          .withColumn("d30_retention", when(col("event_date") === date_add(col("install_date"), 30), 1).otherwise(0))

        println("Number of join partitions: " + join.rdd.getNumPartitions)

        val j = join.select("*")
          .groupBy("language", "device_model", "ip_country", "app_version", "os_version", "install_date")
          .agg(
            count("id").alias("loggin_count")
            , sum("d1_retention").alias("d1_retention")
            , sum("d7_retention").alias("d7_retention")
            , sum("d15_retention").alias("d15_retention")
            , sum("d30_retention").alias("d30_retention"))

        j.show()
        complete("Check logs")
      } ~ path("events" / "totals" / "60days") {
        val events = cache.get("events_totals_60_days", new Callable[Dataset[Row]] {
          override def call(): Dataset[Row] =  {
            val result = ssc.read
              .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test_dump", "table" -> "events"))
              .load()
              .where("event_at > date_sub(current_timestamp(), 61) and event_id in (4, 5, 9, 16)")

            result.cache() // mark cached in Spark internals to allow re-use
          }
        })
        println("Number of events partitions: " + events.rdd.getNumPartitions)

        val devices = cache.get("devices", new Callable[Dataset[Row]] {
          override def call(): Dataset[Row] =  {
            val result = ssc.read
              .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test_dump", "table" -> "devices"))
              .load()

            result.cache() // mark cached in Spark internals to allow re-use
          }
        })
        println("Number of devices partitions: " + devices.rdd.getNumPartitions)

        val join = events
          .join(devices, Seq("device_id"), "left")
          .withColumn("install_date", col("received_at").cast(DataTypes.DateType))
          .withColumn("event_date", col("event_at").cast(DataTypes.DateType))
          .withColumn("games_played", when(col("event_id") === 5, 1).otherwise(0))
          .withColumn("sessions_played", when(col("event_id") === 4, 1).otherwise(0))
          .withColumn("gross_revenue", when((col("event_id") === 9).and(col("attr_3").isNotNull), 1).otherwise(0))
          .withColumn("requests_sent_attr_1", when((col("event_id") === 16).and(col("attr_1").isNotNull), 1).otherwise(0))
          .withColumn("requests_sent_attr_2", when((col("event_id") === 16).and(col("attr_2").isNotNull), 1).otherwise(0))

        println("Number of join partitions: " + join.rdd.getNumPartitions)

        val j = join.select("*")
          .groupBy("install_date")
          .agg(
            sum("games_played").alias("games_played")
            , sum("sessions_played").alias("sessions_played")
            , sum("gross_revenue").alias("gross_revenue")
            , sum("requests_sent_attr_1").alias("requests_sent")
            , sum("requests_sent_attr_2").alias("requests_sent"))

        j.show()
        complete("Check logs")
      }
    }
  }

  def run(ssc: SparkSession): Unit = {
      implicit val system = ActorSystem()
      implicit val materializer = ActorMaterializer()
      import system.dispatcher

      val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
        Http().bind(interface = "localhost", port = 9099)
      val sink = Sink.foreach[Http.IncomingConnection](_.handleWith(routes(ssc)))

      serverSource.to(sink).run
  }
}