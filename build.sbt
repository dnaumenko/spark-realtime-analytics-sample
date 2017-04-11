name := "spark-analytics-sample"
version := "1.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0"

assemblyMergeStrategy in assembly := {
 case entry => {
   val strategy = (assemblyMergeStrategy in assembly).value(entry)
   if (strategy == MergeStrategy.deduplicate) MergeStrategy.first
   else strategy
 }
}
