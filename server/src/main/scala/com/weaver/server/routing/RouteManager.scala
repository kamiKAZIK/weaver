package com.weaver.server.routing

import java.net.{URL, URLClassLoader}
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentLinkedQueue

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.weaver.execution.api.ExecutionProvider
import com.weaver.server.spark.SessionManager

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

object RouteManager {
  private val registry: ConcurrentLinkedQueue[Route] = new ConcurrentLinkedQueue[Route]

  def routes: Route = registry.toSeq.reduce(_ ~ _)

  def register(route: Route): Unit = registry.add(route)

  def register(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    val classLoader = new URLClassLoader(Array(new URL("file:///home/ekazakas/git/kami/weaver/example/target/scala-2.11/example_2.11-0.1-SNAPSHOT.jar")), getClass.getClassLoader)

    val services = ServiceLoader.load(classOf[ExecutionProvider], classLoader)
    val session = SessionManager.get("test").session
    session.sparkContext.addJar("file:///home/ekazakas/git/kami/weaver/example/target/scala-2.11/example_2.11-0.1-SNAPSHOT.jar")
    services.foreach(service => register(service.provide(session)))
  }
}
