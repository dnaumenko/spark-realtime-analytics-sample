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
  val scala = "2.11.8"
  val spark = "2.1.0"
  val akka = "10.0.5"
  val akkaHttpDsl = "2.4.11"
  val cassandraConnector = "2.0.1"

  val gatling = "2.2.5"
}

object Dependencies {
  import Versions._
  import Compile._
  import Test._

  object Compile {
    val sparkCore = "org.apache.spark" % "spark-core_2.11" % spark
    val sparkSql = "org.apache.spark" % "spark-sql_2.11" % spark
    val sparkHive = "org.apache.spark" % "spark-hive_2.11" % spark
    val sparkHiveThriftServer = "org.apache.spark" % "spark-hive-thriftserver_2.11" % spark
    val sparkCassandra = "com.datastax.spark" % "spark-cassandra-connector_2.11" % cassandraConnector

    val akkaCore = "com.typesafe.akka" % "akka-http-core_2.11" % akka
    val akkaHttp = "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaHttpDsl
    val akkaHttpSpray = "com.typesafe.akka" % "akka-http-spray-json_2.11" % akka

    val guava = "com.google.guava" % "guava" % "21.0"
  }

  object Test {
    val gatlingCharts =  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatling
  }

  val akkaAll = Seq(akkaCore, akkaHttp, akkaHttpSpray)
  val sparkAll = Seq(sparkCore, sparkSql, sparkHive, sparkHiveThriftServer, sparkCassandra)
  val gatlingAll = Seq(gatlingCharts)

  val core: Seq[sbt.ModuleID] = Seq(guava) ++ sparkAll ++ akkaAll
}