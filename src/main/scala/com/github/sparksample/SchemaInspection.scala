package com.github.sparksample

import com.github.sparksample.ProcessTextFile.sparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession


object SchemaInspection {
  def main(args: Array[String]): Unit = {
    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
    val sc = new SparkContext(conf)
    val ssc = SparkSession.builder().config(conf).getOrCreate()

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
