package com.github.sparksample

import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.http

import scala.concurrent.duration._

class SimpleServerSimulation extends Simulation {
  object Queries {
    val retentionForAllPeriod = exec(http("events/retention").get("/api/events/retention"))
  }

  val httpConf: Protocol = http
    .baseURL("http://localhost:9099")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn: ScenarioBuilder = scenario("Retention Analysis").exec(Queries.retentionForAllPeriod)

  setUp(
    scn.inject(rampUsers(60) over (30 seconds))
  ).protocols(httpConf)
}