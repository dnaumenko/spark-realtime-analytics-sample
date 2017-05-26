package com.github.sparksample.config

import org.apache.spark.{SparkConf, SparkContext}

trait SparkConfig {
  def sparkConf(appName: String, standaloneMode: Boolean = false): SparkConf = new SparkConf()
    .setAppName(appName)
    .set("spark.scheduler.mode", "FIFO")
    .set("spark.sql.shuffle.partitions", "1")
    .set("spark.default.parallelism", "1") // good for small tasks
    .setMaster(if (standaloneMode) "local[2]" else "spark://localhost:7077") // local[2] cause we need more than one thread to Spark Streaming operate locally
}
