package com.github.sparksample.config

import org.apache.spark.{SparkConf, SparkContext}

trait SparkConfig {
  def sparkConf(appName: String, standaloneMode: Boolean = false): SparkConf = new SparkConf()
    .setAppName(appName)
    .setMaster(if (standaloneMode) "local" else "spark://localhost:7077")
}
