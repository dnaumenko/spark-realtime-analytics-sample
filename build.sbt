name := "spark-analytics-sample"
version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "2.1.0"
val akkaVersion = "10.0.5"
val akkaHttpDslVersion = "2.4.11"

// can be handled by sbt-spark-package plugin
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-hive_2.11" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-hive-thriftserver_2.11" % sparkVersion
libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.1"

libraryDependencies += "com.typesafe.akka" % "akka-http-core_2.11" % akkaVersion
libraryDependencies += "com.typesafe.akka" % "akka-http-spray-json_2.11" % akkaVersion
libraryDependencies += "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaHttpDslVersion

assemblyMergeStrategy in assembly := {
 entry =>
   val strategy = (assemblyMergeStrategy in assembly).value(entry)
   if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
   else strategy
}
