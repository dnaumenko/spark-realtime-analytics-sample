package com.github.sparksample

import com.github.sparksample.ProcessTextFile.sparkConf
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

object WorldCountFromSocket extends App {
  val conf = sparkConf(this.getClass.getName, standaloneMode = true)
  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(sc, Seconds(10))

  val lines = ssc.socketTextStream("localhost", 9999)

  val words = lines.flatMap(_.split(" "))
  val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
  wordCounts.print()
  ssc.start()
  ssc.awaitTermination()
}

object KafkaStreaming extends App {
  val conf = sparkConf(this.getClass.getName, standaloneMode = true)
  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(sc, Seconds(10))

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092"
    , "key.deserializer" -> classOf[StringDeserializer]
    , "value.deserializer" -> classOf[StringDeserializer]
    , "group.id" -> "spark_streaming_test"
    , "auto.offset.reset" -> "latest"
  )

  val topics = Array("dev-kafka-vortex")

  val directKafkaStream = KafkaUtils.createDirectStream(
    ssc
    , PreferConsistent
    , Subscribe[String, String](topics, kafkaParams)
  )

  directKafkaStream.map(record => record.value).print()

  ssc.start()
  ssc.awaitTermination()
}

