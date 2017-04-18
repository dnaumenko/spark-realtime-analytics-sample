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

final case class Identity(id: Long)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val identityFormat: RootJsonFormat[Identity] = jsonFormat1(Identity)
}

object SimpleServer extends Directives with JsonSupport {

  def routes(sc: SparkContext): Route =
    path("api" / "identity" / "count") {
      get {
        val table = sc.cassandraTable("fingerprint", "identities")
        val count = table.count()

        complete(s"Identities count: $count")
      }
    }

  def run(sc: SparkContext): Unit = {
      implicit val system = ActorSystem()
      implicit val materializer = ActorMaterializer()
      import system.dispatcher

      val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
        Http().bind(interface = "localhost", port = 9099)
      val sink = Sink.foreach[Http.IncomingConnection](_.handleWith(routes(sc)))

      serverSource.to(sink).run
  }
}