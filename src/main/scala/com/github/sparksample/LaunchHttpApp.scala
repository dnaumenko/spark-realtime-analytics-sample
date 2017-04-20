package com.github.sparksample

import com.github.sparksample.config.SparkConfig
import com.github.sparksample.http.SimpleServer
import org.apache.spark.SparkContext

object LaunchHttpApp extends SparkConfig {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(sparkConf(this.getClass.getName, standaloneMode = true))

    SimpleServer.run(sc)
  }
}
