# spark-realtime-analytics-sample

It's a simple project to illustrate how Spark can be used as an analytics app backend.

Basically, we want to show following things:
* Working with various data sources - connect, query
* Write data via Streaming
* Provide real-time metrics for dashboards
* Inspect schema for connected datasource dynamically

Spark version used - 2.1.0

# Dependencies

## Spark
 
There is a Docker compose script for simple cluster with master and one worker.
It's based on [gettyimages/docker-spark](https://github.com/gettyimages/docker-spark).

Simply run `docker-compose up` to get the running cluster on your machine.
By default, it binds master's Spark UI to [localhost:8081](http://localhost:8081). Worker node use 4G of memory by default.

In addition, it binds spark-default.conf from this repo `conf` directory to a container.
You can override the defaults there.

If you need to pass the local files to worker nodes, there is a /tmp/data folder already available.
Use Docker to make a binding to your local folder.

(Optionally) Download and install the latest version of Spark [here](http://d3kbcqa49mib13.cloudfront.net/spark-2.1.0-bin-hadoop2.7.tgz) 
to get command line tools at hand (e.g. `spark-submit` script).

## Cassandra

You should specify the Cassandra host for jobs.

If you use spark-submit script locally, 
update your conf/spark-defaults.conf in Spark's home with following line:
```
# Cassandra configs
spark.cassandra.connection.host=192.168.0.175 # put your host here
```

If you use spark-submit script from Docker via exec, put the changes above to `./conf/{master,worker}/spark-defaults.conf`.

The third option is to put host programmatically via `conf.set("spark.cassandra.connection.host", "192.168.0.175")` in examples code.

# How to start

There are examples to launch as stand-alone apps and you have many options how to submit jobs to Spark cluster:
1. you can use /bin/spark-submit script. See [docs](http://spark.apache.org/docs/latest/submitting-applications.html) for details. 
Example:
```
> spark-submit --class com.github.sparksample.ConnectSparkToCassandra --master localhost:7077 ./target/scala-2.11/spark-analytics-sample-assembly-1.0.jar
```
2. You can submit jobs programmatically, via SparkLauncher. It's a builder on top of spark-submit process.
But it's quite difficult to use. You should have a separate application and jar with spark jobs loaded as resource.
See [this doc](http://henningpetersen.com/post/22/running-apache-spark-jobs-from-applications) for some details.

3. REST API for managing Spark jobs/jars via [spark-jobserver](https://github.com/spark-jobserver/spark-jobserver)
 or something similar

4. Use a long-running app with REST API (as a driver program) that uses Spark Context like DB.

# Local testing/debugging

Local cluster is a good option when you want to debug/experiment with Spark during development.
In this case, you don't need a running cluster. To enable it, set the standaloneMode = true during Spark config init:
> val conf = sparkConf(this.getClass.getName, **standaloneMode = true**)

# Load Test with Gatling

There is also a separate module (loadtool) not included in assembly jar that is used for load tests. You can run a simple scenario via `Engine` class 
from IDE. The code and usage is trivial. Check [Gatling](http://gatling.io/docs/current/) for details. 

## Issues found during testing
 
* Concurrent SQL requests fail from time to time (should be fixed in 2.2.0) - 
https://issues.apache.org/jira/browse/SPARK-13747 (tried to downgrade to Spark 1.6.3 in separate branch w/o luck). Switching to thread-pool executor helps.
   
* Spark-submit has a lot of common libs already provided in SPARK_HOME/lib. Unfortunately, it uses
  the old Guice 3.0 and relies on Slf4j over Log4J (i.e. if your app depends on Logback, you are in trouble).
  Can be fixed if you provide --driver-class-path=<path_to_assembly_jar>.
  
* Deduplication issues on jar assembly - specify ```case PathList("org", "apache", "spark", xs@_*) => MergeStrategy.last```
in assembly strategy.

# Resources

## Spark
* http://spark.apache.org/docs/latest/sql-programming-guide.html - official doc

## Cassandra-connector

* https://github.com/datastax/spark-cassandra-connector/blob/master/doc/1_connecting.md

## Redshift-connector

* https://github.com/databricks/spark-redshift

## Thrift JDBC/ODBC Server

* https://forums.databricks.com/questions/1464/how-to-configure-thrift-server-to-use-a-custom-spa.html - most useful discussion about the subject
* https://github.com/inadco/cassandra-spark-jdbc-bridge - old versions used, but illustrates the idea 
