package com.weaver.example

import akka.http.scaladsl.server.{Directives, Route}
import com.weaver.execution.api.ExecutionProvider
import org.apache.spark.sql.SparkSession

class SequenceExample extends ExecutionProvider with Directives {
  override def provide(sparkSession: SparkSession): Route = path("seq") {
    get {
      println("HERE!")
      val sequence = Seq(1, 2, 3, 4, 5, 6)
      val rdd = sparkSession.sparkContext.parallelize(sequence)
      val result = rdd.reduce(_ + _)

      complete(s"Result $result")
    }
  }
}
