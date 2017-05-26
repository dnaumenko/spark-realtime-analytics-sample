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
  val kafkaIntegration = "2.1.1"

  val redshiftVersion = "1.11.133"

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
    val sparkStreaming = "org.apache.spark" % "spark-streaming_2.11" % spark
    val sparkStreamingKafka = "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % kafkaIntegration

    val sparkCassandra = "com.datastax.spark" % "spark-cassandra-connector_2.11" % cassandraConnector

    val akkaCore = "com.typesafe.akka" % "akka-http-core_2.11" % akka
    val akkaHttp = "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaHttpDsl
    val akkaHttpSpray = "com.typesafe.akka" % "akka-http-spray-json_2.11" % akka

    val redshift = "com.amazonaws" % "aws-java-sdk" % redshiftVersion
    val redshiftJdbc = "com.amazon.redshift" % "jdbc4" % "1.2.1.1001" from "https://s3.amazonaws.com/redshift-downloads/drivers/RedshiftJDBC42-1.2.1.1001.jar"
    val scalalikeJdbc = "org.scalikejdbc" % "scalikejdbc_2.11" % "3.0.0"

    val guava = "com.google.guava" % "guava" % "21.0"
  }

  object Test {
    val gatlingCharts =  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatling
  }

  val akkaAll = Seq(akkaCore, akkaHttp, akkaHttpSpray)
  val sparkAll = Seq(sparkCore, sparkSql, sparkHive, sparkHiveThriftServer, sparkCassandra,
    sparkStreaming, sparkStreamingKafka)
  val gatlingAll = Seq(gatlingCharts)
  val amazonAll = Seq(redshift, redshiftJdbc, scalalikeJdbc)

  val core: Seq[sbt.ModuleID] = Seq(guava) ++ sparkAll ++ akkaAll ++ amazonAll
}