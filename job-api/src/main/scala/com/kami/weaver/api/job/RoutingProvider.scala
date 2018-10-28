package com.kami.weaver.api.job

import akka.http.scaladsl.server.Route
import org.apache.spark.sql.SparkSession

trait RoutingProvider {
  def provide(sparkSession: SparkSession): Route
}
