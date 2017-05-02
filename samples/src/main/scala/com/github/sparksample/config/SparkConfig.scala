package com.github.sparksample.config

import org.apache.spark.{SparkConf, SparkContext}

trait SparkConfig {
  def sparkConf(appName: String, standaloneMode: Boolean = false): SparkConf = new SparkConf()
    .setAppName(appName)
    .set("spark.scheduler.mode", "FAIR")
    .set("spark.sql.shuffle.partitions", "1")
    .setMaster(if (standaloneMode) "local" else "spark://localhost:7077")
}
