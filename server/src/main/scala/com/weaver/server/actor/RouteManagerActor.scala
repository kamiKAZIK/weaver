package com.weaver.server.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import com.weaver.server.routing.RouteManager

object RouteManagerActor {
  case object SetUp
  case object SetUpCompleted
  case class SetUpFailed(e: Throwable)

  def props: Props = Props(new RouteManagerActor)
}

class RouteManagerActor extends Actor
  with ActorLogging {

  import context.dispatcher

  override def receive: Receive = {
    case RouteManagerActor.SetUp =>
      RouteManager.register
        .map(_ => RouteManagerActor.SetUpCompleted)
        .recover {
          case e: Throwable =>
            RouteManagerActor.SetUpFailed(e)
        }.pipeTo(sender)
    case _ =>
      log.error("Unknown message received!")
  }
}
