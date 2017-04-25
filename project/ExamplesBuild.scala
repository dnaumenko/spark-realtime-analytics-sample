import sbt._
import sbt.Keys._
import sbtassembly.AssemblyPlugin.autoImport._

object ExamplesBuild extends Build {
  val baseSettings = Seq(
    scalaVersion := Versions.scala
  )

  override lazy val settings = super.settings ++ baseSettings

  lazy val root = Project(
    id = "root",
    base = file("."),
    aggregate = Seq(samples)
  ).settings(
    assemblyMergeStrategy in assembly := {
      entry =>
        val strategy = (assemblyMergeStrategy in assembly).value(entry)
        if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
        else strategy
    }
  )

  lazy val samples = Project(
    id = "samples",
    base = file("./samples"),
    settings = Seq(libraryDependencies ++= Dependencies.core)
  ).settings(
    assemblyMergeStrategy in assembly := {
      entry =>
        val strategy = (assemblyMergeStrategy in assembly).value(entry)
        if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
        else strategy
    }
  )

  lazy val loadtool = Project(
    id = "loadtool",
    base = file("./loadtool"),
    settings = Seq(libraryDependencies ++= Dependencies.gatlingAll)
  ).settings(
    assemblyMergeStrategy in assembly := {
      entry =>
        val strategy = (assemblyMergeStrategy in assembly).value(entry)
        if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
        else strategy
    }
  )
}

object Versions {
  val scala = "2.10.5"
  val spark = "1.6.3"
  val akka = "10.0.5"
  val akkaHttpDsl = "2.0.5"
  val cassandraConnector = "1.6.6"

  val gatling = "2.0.3"
}

object Dependencies {
  import Versions._
  import Compile._
  import Test._

  object Compile {
    val sparkCore = "org.apache.spark" % "spark-core_2.10" % spark % "provided"
    val sparkSql = "org.apache.spark" % "spark-sql_2.10" % spark % "provided"
    val sparkHive = "org.apache.spark" % "spark-hive_2.10" % spark % "provided"
    val sparkHiveThriftServer = "org.apache.spark" % "spark-hive-thriftserver_2.10" % spark % "provided"
    val sparkCassandra = "com.datastax.spark" % "spark-cassandra-connector_2.10" % cassandraConnector

    val akkaCore = "com.typesafe.akka" % "akka-actor_2.10" % "2.3.16"
    val akkaHttp = "com.typesafe.akka" % "akka-http-experimental_2.10" % akkaHttpDsl
    val akkaHttpSpray = "io.spray" % "spray-json_2.10" % "1.3.3"
  }

  object Test {
    val gatlingCharts =  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatling
  }

  val akkaAll = Seq(akkaCore, akkaHttp, akkaHttpSpray)
  val sparkAll = Seq(sparkCore, sparkSql, sparkHive, sparkHiveThriftServer, sparkCassandra)
  val gatlingAll = Seq(gatlingCharts)

  val core: Seq[sbt.ModuleID] = sparkAll ++ akkaAll
}