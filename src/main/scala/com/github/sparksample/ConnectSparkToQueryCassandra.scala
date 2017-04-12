package com.github.sparksample

import com.github.sparksample.ConnectSparkToProcessTextFile.sparkConf
import org.apache.spark.SparkContext
import com.datastax.spark.connector._

object ConnectSparkToQueryCassandra {
  def main(args: Array[String]): Unit = {
    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
    // could be set dynamically via conf/spark-default.conf in spark home on client's machine
    // conf.set("spark.cassandra.connection.host", "192.168.0.175")
    val sc = new SparkContext(conf)

    val table = sc.cassandraTable("test_keyspace", "test_table")
    val rowCount = table.count()
    table.saveAsTextFile("/tmp/output.txt")

    println(s"Total rows in table: $rowCount")
  }
}
