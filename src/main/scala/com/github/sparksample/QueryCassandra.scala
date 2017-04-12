package com.github.sparksample

import com.github.sparksample.ProcessTextFile.sparkConf
import org.apache.spark.SparkContext
import com.datastax.spark.connector._
import org.apache.spark.sql.{SQLContext, SparkSession}

/*
   Note: You could set Cassandra's host ynamically via conf/spark-default.conf in spark home on client's machine.
   Otherwise, do it manually via:
   // conf.set("spark.cassandra.connection.host", "192.168.0.175")
 */
object QueryCassandraViaRDD {
  def main(args: Array[String]): Unit = {
    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
    val sc = new SparkContext(conf)

    val table = sc.cassandraTable("test_keyspace", "test_table")
    val rowCount = table.count()
    table.saveAsTextFile("/tmp/output.txt")

    println(s"Total rows in table: $rowCount")
  }
}


object QueryCassandraViaDataSets {
  /*
  See https://github.com/datastax/spark-cassandra-connector/blob/master/doc/14_data_frames.md for details
   */
  def main(args: Array[String]): Unit = {
    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
    val sc = new SparkContext(conf)
    val ssc = SparkSession.builder().config(conf).getOrCreate()

    val df = ssc.read
      .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "fingerprint", "table" -> "identities"))
      .load()

    df.show()
  }
}
