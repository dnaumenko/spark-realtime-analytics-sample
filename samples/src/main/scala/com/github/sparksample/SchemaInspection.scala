package com.github.sparksample

import com.github.sparksample.QueryCassandraViaDataSets.sparkConf
import com.github.sparksample.config.SparkConfig
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext


object SchemaInspection extends SparkConfig {
  def main(args: Array[String]): Unit = {
    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
    val sc = new SparkContext(sparkConf(this.getClass.getName))
    val ssc = new SQLContext(sc)

    val df = ssc.read
      .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "fingerprint", "table" -> "identities"))
      .load()

    val schema = df.schema
    println(s"Field names ${schema.fieldNames.mkString(",")}")
    schema.fields.foreach { field =>
      println(s"Field ${field.name}, nullable: ${field.nullable}, type ${field.dataType}")
    }
  }
}
