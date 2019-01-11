package com.weaver.execution.api

import akka.http.scaladsl.server.Route
import org.apache.spark.sql.SparkSession

trait ExecutionProvider {
  def provide(sparkSession: SparkSession): Route
}
