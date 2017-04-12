name := "spark-analytics-sample"
version := "1.0"

scalaVersion := "2.11.8"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.1.0"
libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.1"

assemblyMergeStrategy in assembly := {
 entry =>
   val strategy = (assemblyMergeStrategy in assembly).value(entry)
   if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
   else strategy
}
