package com.weaver.server.routing

import java.net.{URL, URLClassLoader}
import java.time.temporal.ChronoUnit
import java.time.{LocalDateTime, Period}
import java.util.{ServiceLoader, UUID}
import java.util.concurrent.ConcurrentLinkedQueue

import akka.event.Logging.LogLevel
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteResult.{Complete, Rejected}
import akka.http.scaladsl.server.{Route, RouteResult}
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LogEntry, LoggingMagnet}
import com.weaver.execution.api.ExecutionProvider
import com.weaver.server.persistence.model.Execution
import com.weaver.server.spark.{PackageManager, SessionManager}

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

object RouteManager {
  private val registry: ConcurrentLinkedQueue[Route] = new ConcurrentLinkedQueue[Route]

  def setUp(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    val classLoader = new URLClassLoader(PackageManager.list.map(new URL(_)).toArray, getClass.getClassLoader)

    val services = ServiceLoader.load(classOf[ExecutionProvider], classLoader)
    services.foreach(service => register(pathPrefix(Segment) { sessionName: String =>
      service.provide(SessionManager.get(sessionName).session)
    }))
  }

  def routes: Route = DebuggingDirectives.logRequestResult(LoggingMagnet(persistTimes)) {
    registry.toSeq.reduce(_ ~ _)
  }

  def register(route: Route): Unit = registry.add(route)

  private def timesLogger(loggingAdapter: LoggingAdapter, requestTimestamp: LocalDateTime)
                         (request: HttpRequest)
                         (result: RouteResult): Unit = {
    val responseTimestamp = LocalDateTime.now()
    val (entry, execution) = result match {
      case Complete(response) =>
        val elapsedTime = requestTimestamp.until(responseTimestamp, ChronoUnit.MILLIS)
        LogEntry(s"Logged Request:${request.method}:${request.uri}:${response.status}:$elapsedTime") ->
          Execution(UUID.randomUUID(), request.uri.toString, requestTimestamp, responseTimestamp, succeeded = true)
      case Rejected(reason) =>
        LogEntry(s"Rejected Reason: ${reason.mkString(",")}") ->
          Execution(UUID.randomUUID(), request.uri.toString, requestTimestamp, responseTimestamp, succeeded = false)
    }
    println(execution)
    entry.logTo(loggingAdapter)
  }

  private def persistTimes(log: LoggingAdapter): HttpRequest => RouteResult => Unit = {
    val requestTimestamp = LocalDateTime.now()
    timesLogger(log, requestTimestamp)
  }
}
