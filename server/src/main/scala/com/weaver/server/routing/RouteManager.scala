package com.weaver.server.routing

import java.net.{URL, URLClassLoader}
import java.time.temporal.ChronoUnit
import java.time.{LocalDateTime, Period}
import java.util.{ServiceLoader, UUID}
import java.util.concurrent.ConcurrentLinkedQueue

import akka.event.Logging.LogLevel
import akka.event.{Logging, LoggingAdapter}
import akka.http.javadsl.server.CustomRejection
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteResult.{Complete, Rejected}
import akka.http.scaladsl.server.directives.BasicDirectives.{extractRequestContext, mapRouteResult}
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LogEntry, LoggingMagnet}
import com.weaver.execution.api.ExecutionProvider
import com.weaver.server.persistence.model.{Execution, ExecutionsRepository}
import com.weaver.server.spark.{PackageManager, SessionManager}

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object RouteManager {
  private val registry: ConcurrentLinkedQueue[Route] = new ConcurrentLinkedQueue[Route]

  def setUp(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    val classLoader = new URLClassLoader(PackageManager.list.map(new URL(_)).toArray, getClass.getClassLoader)

    val services = ServiceLoader.load(classOf[ExecutionProvider], classLoader)
    services.foreach(service => register(pathPrefix(Segment) { sessionName: String =>
      service.provide(SessionManager.get(sessionName).session)
    }))
  }

  def routes(implicit executionContext: ExecutionContext): Route = logExecution(executionContext) {
    registry.toSeq.reduce(_ ~ _)
  }

  def register(route: Route): Unit = registry.add(route)

  private[this] def logExecution(implicit executionContext: ExecutionContext): Directive[Unit] =
    extractRequestContext.flatMap { ctx =>
      val execution = Execution(method = ctx.request.method.value, uri = ctx.request.uri.toString)
      onSuccess(ExecutionsRepository.insert(execution)).flatMap { r =>
        mapRouteResultFuture { result =>
          result.flatMap {
            case Complete(_) =>
              ExecutionsRepository.markAsSucceeded(execution)
                .flatMap(_ => result)
            case Rejected(_) =>
              ExecutionsRepository.markAsFailed(execution)
                .flatMap(_ => result)
          }
        }
      }
    }
}


/*def routes(implicit executionContext: ExecutionContext) =
  extractRequestContext.flatMap { ctx =>
    val execution = Execution(method = ctx.request.method.value, uri = ctx.request.uri.toString)
    onSuccess(ExecutionsRepository.insert(execution)).flatMap { r =>
      mapRouteResultFuture { result =>
        result.flatMap {
          case Complete(_) =>
            ExecutionsRepository.markAsSucceeded(execution)
              .flatMap(_ => result)
          case Rejected(_) =>
            ExecutionsRepository.markAsFailed(execution)
              .flatMap(_ => result)
        }
      }
    }
  }*/