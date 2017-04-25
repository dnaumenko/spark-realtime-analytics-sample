package com.github.sparksample

import com.github.sparksample.ProcessTextFile.sparkConf
import com.github.sparksample.SchemaInspection.sparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.thriftserver.HiveThriftServer2
//import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.sql.types.{DataType, StringType, StructField, StructType}

/*
  An example to parse plain text file and expose it via Thrift JDBC server
// */
//object ThriftRDDSharing {
//  def main(args: Array[String]): Unit = {
//    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
//    val sc = new SparkContext(sparkConf(this.getClass.getName))
//    val sql  = new SQLContext(sc)
//
//    val data = ssc.sparkContext.textFile("/tmp/data/data.txt").map(l => Row.fromSeq(Seq(l)))
//    val df = sql.createDataFrame(data, schema = StructType(Seq(StructField("test_column", StringType))))
//
//    df.registerTempTable("test_table")
//
//    HiveThriftServer2.startWithContext(sql)
//
//    while (true) {
//      Thread.`yield`()
//    }
//  }
//}
