package com.weaver.server.actor

import akka.actor.{Actor, ActorLogging}

object SessionManagerActor {
  case object SetUp
  case class CreateSession(name: String)
}

class SessionManagerActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case SessionManagerActor.SetUp => {

    }
    case SessionManagerActor.CreateSession(name) =>

  }
}
