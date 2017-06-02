package com.github.sparksample

import java.util.Date

import com.github.sparksample.ProcessTextFile.sparkConf
import org.apache.spark.sql.SparkSession

object QueryRedshift extends App {
  val conf = sparkConf(this.getClass.getName, standaloneMode = true)
  val ssc = SparkSession.builder().config(conf).getOrCreate()

  ssc.sparkContext.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", "AKIAIVHRPJ6N2D33XP2A")
  ssc.sparkContext.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", "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")

  val df = ssc.read
    .format("com.databricks.spark.redshift")
    .option("url", "jdbc:redshift://dev-rs.cekneo1q5z4c.us-west-2.redshift.amazonaws.com:5439/dev?user=platform_team&password=63ezKrxtTvr7")
    .option("query", "select count(*) from test_db.users")
//    .option("aws_iam_role", "arn:aws:iam::956613775090:role/dev-rs")
    .option("tempdir", "s3n://devops.playq/redshift/dev/import")
    .option("jdbcdriver", "com.amazon.redshift.jdbc42.Driver")
    .load()

  df.show()
}

object QueryRedshiftDirect extends App {
  import scalikejdbc._

  Class.forName("com.amazon.redshift.jdbc42.Driver")
  ConnectionPool.singleton("jdbc:redshift://dev-rs.cekneo1q5z4c.us-west-2.redshift.amazonaws.com:5439/dev",
    "platform_team", "63ezKrxtTvr7")
  implicit val session = AutoSession

  val result = sql"select * from test_db.users".map(_.toMap).list.apply()
  println(result.size)
}

object WriteRedshift extends App {
  case class User(userid: Long, username: String)

  val idStart = new Date().getTime % Int.MaxValue


  val conf = sparkConf(this.getClass.getName, standaloneMode = true)
  val ssc = SparkSession.builder().config(conf).getOrCreate()

  ssc.sparkContext.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", "AKIAIVHRPJ6N2D33XP2A")
  ssc.sparkContext.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", "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")

  val testDump = Seq(User(idStart, "User 1"), User(idStart + 1, "User 2"))
  val df = ssc.createDataFrame(testDump)

  df.write
    .format("com.databricks.spark.redshift")
    .option("url", "jdbc:redshift://dev-rs.cekneo1q5z4c.us-west-2.redshift.amazonaws.com:5439/dev?user=platform_team&password=63ezKrxtTvr7")
    .option("dbtable", "test_db.users")
    .option("tempdir", "s3n://devops.playq/redshift/dev/import")
    .option("jdbcdriver", "com.amazon.redshift.jdbc42.Driver")
    .mode("append")
    .save()
}
