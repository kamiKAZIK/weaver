package com.kami.weaver.server.routing

import java.net.{URL, URLClassLoader}
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentLinkedQueue

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.kami.weaver.api.job.RoutingProvider
import com.kami.weaver.server.spark.SessionManager

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

object RouteManager {
  private val registry: ConcurrentLinkedQueue[Route] = new ConcurrentLinkedQueue[Route]

  def routes: Route = registry.toSeq.reduce(_ ~ _)

  def register(route: Route): Unit = registry.add(route)

  def register(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    val classLoader = new URLClassLoader(Array(new URL("file:///home/ekazakas/git/kami/example/target/scala-2.11/example_2.11-0.1.jar")), getClass.getClassLoader)

    val services = ServiceLoader.load(classOf[RoutingProvider], classLoader)
    services.foreach(service => register(service.provide(SessionManager.get("test").session)))
  }
}
