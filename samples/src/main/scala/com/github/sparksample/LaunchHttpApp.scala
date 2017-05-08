package com.github.sparksample

import com.github.sparksample.config.SparkConfig
import com.github.sparksample.httpapp.SimpleServer
import org.apache.spark.sql.SparkSession

object LaunchHttpApp extends SparkConfig {
  def main(args: Array[String]): Unit = {
    val conf = sparkConf(this.getClass.getName, standaloneMode = true)
    val ssc = SparkSession.builder().config(conf).getOrCreate()

    SimpleServer.run(ssc)
  }
}
