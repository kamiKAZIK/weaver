package com.kami.weaver.server.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import com.kami.weaver.server.persistence.management.TableManager

object SchemaManagerActor {
  case object SetUp
  case object SetUpCompleted
  case class SetUpFailed(e: Throwable)

  def props: Props = Props(new SchemaManagerActor)
}

class SchemaManagerActor extends Actor
  with ActorLogging {

  import context.dispatcher

  override def receive: Receive = {
    case SchemaManagerActor.SetUp =>
      TableManager
        .setUp
        .map(_ => SchemaManagerActor.SetUpCompleted)
        .recover {
          case e: Throwable =>
            SchemaManagerActor.SetUpFailed(e)
        }.pipeTo(sender())
    case _ =>
      log.error("Unknown message received!")
  }
}
