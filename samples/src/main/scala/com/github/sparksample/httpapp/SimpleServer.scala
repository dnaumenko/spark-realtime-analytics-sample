package com.github.sparksample.httpapp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import spray.json._

import scala.concurrent.Future
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes

final case class Identity(id: Long)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val identityFormat: RootJsonFormat[Identity] = jsonFormat1(Identity)
}

object SimpleServer extends Directives with JsonSupport {

  def routes(ssc: SparkSession): Route = {
    pathPrefix("api") {
      path("events" / "count") {
        get {
          val df = ssc.read
            .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test", "table" -> "events"))
            .load()
          val count = df.count()

          complete(s"Identities count: $count")
        }
      } ~ path("events" / "retention") {
        val events = ssc.read
          .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test", "table" -> "events"))
          .load()
          .where("event_id == 4")

        val devices = ssc.read
          .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test", "table" -> "devices"))
          .load()

        val join = events
          .join(devices, Seq("device_id"), "left")
          .withColumn("install_date", col("received_at").cast(DataTypes.DateType))
          .withColumn("event_date", col("event_at").cast(DataTypes.DateType))
          .withColumn("d1_retention", when(col("event_date") === date_add(col("install_date"), 1), 1).otherwise(0))
          .withColumn("d7_retention", when(col("event_date") === date_add(col("install_date"), 7), 1).otherwise(0))
          .withColumn("d15_retention", when(col("event_date") === date_add(col("install_date"), 15), 1).otherwise(0))
          .withColumn("d30_retention", when(col("event_date") === date_add(col("install_date"), 30), 1).otherwise(0))

        val j = join.select("*")
          .groupBy("platform_name", "language", "device_model", "ip_country", "app_version", "os_version", "install_date")
          .agg(
            count("id").alias("loggin_count")
            , sum("d1_retention").alias("d1_retention")
            , sum("d7_retention").alias("d7_retention")
            , sum("d15_retention").alias("d15_retention")
            , sum("d30_retention").alias("d30_retention"))

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