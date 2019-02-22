package com.weaver.server.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import com.weaver.server.spark.{PackageManager, SessionManager}

object SessionManagerActor {
  case object SetUp
  case object SetUpCompleted
  case class SetUpFailed(e: Throwable)

  def props: Props = Props(new SessionManagerActor)
}

class SessionManagerActor extends Actor
  with ActorLogging {

  import context.dispatcher

  override def receive: Receive = {
    case SessionManagerActor.SetUp =>
      SessionManager.setUp(context.system.settings.config, PackageManager.list)
        .map(_ => SessionManagerActor.SetUpCompleted)
        .recover {
          case e: Throwable => SessionManagerActor.SetUpFailed(e)
        }
        .pipeTo(sender)
    case _ =>
      log.error("Unknown message received!")
  }
}
