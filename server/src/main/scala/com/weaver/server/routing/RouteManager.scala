package com.weaver.server.routing

import java.net.{URL, URLClassLoader}
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentLinkedQueue

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.Config
import com.weaver.execution.api.ExecutionProvider
import com.weaver.server.spark.{PackageManager, SessionManager}

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

object RouteManager {
  private val registry: ConcurrentLinkedQueue[Route] = new ConcurrentLinkedQueue[Route]

  def routes: Route = registry.toSeq.reduce(_ ~ _)

  def register(route: Route): Unit = registry.add(route)

  def setUp(config: Config)(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    val classLoader = new URLClassLoader(PackageManager.list.map(new URL(_)).toArray, getClass.getClassLoader)

    val services = ServiceLoader.load(classOf[ExecutionProvider], classLoader)
    services.foreach(service => register(pathPrefix(Segment) { sessionName: String =>
      service.provide(SessionManager.get(sessionName).session)
    }))
  }
}
