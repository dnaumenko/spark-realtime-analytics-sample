package com.github.sparksample

import com.github.sparksample.config.SparkConfig
import org.apache.spark.{SparkConf, SparkContext}

/*
 Simple test case for processing a plain text file.
 */
object ConnectSparkToProcessTextFile extends SparkConfig {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(sparkConf(this.getClass.getName))
    val len = sc.textFile("/tmp/data/data.txt").map(s => s.length).reduce(_ + _)
    println(s"Total length of file: $len")
  }
}
