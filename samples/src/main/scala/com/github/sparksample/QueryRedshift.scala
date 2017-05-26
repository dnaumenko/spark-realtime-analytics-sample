package com.github.sparksample

import com.github.sparksample.ProcessTextFile.sparkConf
import org.apache.spark.sql.SparkSession

object QueryRedshift extends App {
  val awsAccessKeyId = args(0)
  val awsSecretAccessKey = args(1)
  val redshiftDBName = args(2)
  val redshiftUserId = args(3)
  val redshiftPassword = args(4)
  val redshifturl = args(5)
  val jdbcURL = s"jdbc:redshift://$redshifturl/$redshiftDBName?user=$redshiftUserId&password=$redshiftPassword"

  val conf = sparkConf(this.getClass.getName, standaloneMode = true)
  val ssc = SparkSession.builder().config(conf).getOrCreate()

  val df = ssc.read
    .format("org.apache.spark.sql.cassandra").options(Map("keyspace" -> "fingerprint", "table" -> "identities"))
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
