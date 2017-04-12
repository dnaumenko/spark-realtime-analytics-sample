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
By default, it binds master's Spark UI to [localhost:7081](localhost:7081).

In addition, it binds spark-default.conf from this repo `conf` directory to a container.
You can override the defaults there.

If you need to pass the local files to worker nodes, there is a /tmp/data folder already available.
Use Docker to make a binding to your local folder

# How to start

There are example that can be launched as stand-alone apps. There are 2 options how to submit jobs to Spark cluster.

First, you can use /bin/spark-submit script. See [docs](http://spark.apache.org/docs/latest/submitting-applications.html) for details.
Example:
> spark-submit --class com.github.sparksample.ConnectSparkToCassandra --master localhost:7077 ./target/scala-2.11/spark-analytics-sample-assembly-1.0.jar

Second, you could submit jobs programmatically, via SparkLauncher.




