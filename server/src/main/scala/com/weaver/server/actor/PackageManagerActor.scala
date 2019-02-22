package com.weaver.server.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import com.weaver.server.spark.PackageManager

object PackageManagerActor {
  case object SetUp
  case object SetUpCompleted
  case class SetUpFailed(e: Throwable)

  def props: Props = Props(new PackageManagerActor)
}

class PackageManagerActor extends Actor
  with ActorLogging {

  import context.dispatcher

  override def receive: Receive = {
    case PackageManagerActor.SetUp =>
      PackageManager
        .setUp(context.system.settings.config)
        .map(_ => PackageManagerActor.SetUpCompleted)
        .recover {
          case e: Throwable =>
            PackageManagerActor.SetUpFailed(e)
        }
        .pipeTo(sender)
    case _ =>
      log.error("Unknown message received!")
  }
}
