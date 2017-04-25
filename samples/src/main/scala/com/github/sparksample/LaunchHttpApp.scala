package com.github.sparksample

import com.github.sparksample.ProcessTextFile.sparkConf
import com.github.sparksample.config.SparkConfig
import com.github.sparksample.httpapp.SimpleServer
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object LaunchHttpApp extends SparkConfig {
  def main(args: Array[String]): Unit = {
    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
    val sc = new SparkContext(sparkConf(this.getClass.getName))
    val ssc = new SQLContext(sc)

    SimpleServer.run(ssc)
  }
}
