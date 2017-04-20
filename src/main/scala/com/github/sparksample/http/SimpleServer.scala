package com.github.sparksample.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.apache.spark.SparkContext
import spray.json._

import scala.concurrent.Future
import com.datastax.spark.connector._
import org.apache.spark.sql.SparkSession

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

        val devices = ssc.read
          .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "spark_test", "table" -> "devices"))
          .load()

        val join = events.join(devices, "device_id")

        join.show()
        complete("TODO")
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