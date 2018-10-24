package com.kami.weaver.server.actor

import akka.actor.{Actor, ActorLogging}

object SessionManagerActor {
  case class CreateSession(name: String)
}

class SessionManagerActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case SessionManagerActor.CreateSession(name) =>

  }
}
