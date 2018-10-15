package com.pointlogic.weaver.spark

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import akka.pattern.gracefulStop
import com.pointlogic.weaver.spark.LocalSessionManager._
import com.typesafe.config.Config

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._

object LocalSessionManagerActor {
  case object Initialize
  case class StartSession(name: String, sessionConfig: Config)
  case class StopSession(name: String)
  case class SessionStartError(t: Throwable)
  case class SessionStopError(t: Throwable)
  case object SessionStopped
  case object ListSessions
  case object NoSuchSession
  case object SessionStarted
  case object SessionAlreadyExists
}

class LocalSessionManager extends Actor
  with ActorLogging {

  private val sessions = mutable.HashMap.empty[String, (ActorRef, ActorRef)]

  //private val config = context.system.settings.config
  //private val defaultContextConfig = config.getConfig("weaver.spark.session-settings")
  //val contextTimeout = SparkJobUtils.getContextCreationTimeout(config)
  //val contextDeletionTimeout = SparkJobUtils.getContextDeletionTimeout(config)

  override def receive: Receive = {
    case Initialize =>

    case ListSessions =>
      sender ! sessions.keys.toSeq
    case StartSession(name, sessionConfig) =>
      /*val mergedConfig = sessionConfig.withFallback(defaultContextConfig)
      if (sessions.contains(name)) {
        sender ! SessionAlreadyExists
      } else {
        startSession(name, mergedConfig, false, contextTimeout) { sessionManager =>
          sender ! SessionStarted
        } { err =>
          sender ! SessionStartError(err)
        }
      }*/
    case StopSession(name) =>
      /*if (sessions.contains(name)) {
        log.info("Shutting down context {}", name)
        try {
          val stoppedCtx = gracefulStop(contexts(name)._1, contextDeletionTimeout.seconds)
          Await.result(stoppedCtx, contextDeletionTimeout + 1.seconds)
          sessions.remove(name)
          sender ! SessionStopped
        } catch {
          case err: Throwable => sender ! SessionStopError(err)
        }
      } else {
        sender ! NoSuchSession
      }*/
  }

  private def startSession(name: String, sessionConfig: Config, timeoutSecs: Int = 1)
                          (successFunc: ActorRef => Unit)
                          (failureFunc: Throwable => Unit) {
    log.info("Creating a SparkSession named {}", name)

    val ref = context.actorOf(JobManagerActor.props(dao), name)
    (ref ? JobManagerActor.Initialize(
      mergedConfig, resultActorRef, dataManagerActor))(Timeout(timeoutSecs.second)).onComplete {
      case Failure(e: Exception) =>
        logger.error("Exception after sending Initialize to JobManagerActor", e)
        // Make sure we try to shut down the context in case it gets created anyways
        ref ! PoisonPill
        failureFunc(e)
      case Success(JobManagerActor.Initialized(_, resultActor)) =>
        logger.info("SparkContext {} initialized", name)
        contexts(name) = (ref, resultActor)
        context.watch(ref)
        successFunc(ref)
      case Success(JobManagerActor.InitError(t)) =>
        ref ! PoisonPill
        failureFunc(t)
      case x =>
        logger.warn("Unexpected message received by startContext: {}", x)
    }
  }
}
