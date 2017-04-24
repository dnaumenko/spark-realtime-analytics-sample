package com.github.sparksample

import org.apache.spark.launcher.SparkLauncher

import scala.io.Source

object Launcher extends App {
  val appResource = ProcessTextFile.getClass.getProtectionDomain.getCodeSource.getLocation.getFile

  val sparkJob = new SparkLauncher()
    .setVerbose(true)
    .setSparkHome("/usr/local/spark")
    .setAppResource(appResource)
    .setMainClass("com.github.sparksample.ProcessTextFile")
//    .addJar(SparkContext.jarOfClass(ProcessTextFile.getClass).get) // to make this work ProcessTextFile should be in a separate uber-jar

  val process = sparkJob.launch()

  println("Waiting for finish...")
  val exitCode = process.waitFor
  println("Finished! Exit code:" + exitCode)
  println("Error: " + Source.fromInputStream(process.getErrorStream).mkString)
  println("Output: " + Source.fromInputStream(process.getInputStream).mkString)
}
