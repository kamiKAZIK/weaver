/*package com.kami.weaver.server.spark

import akka.actor.ActorRef

import scala.collection.concurrent.TrieMap

object JobResultManager {
  private val subscribers: TrieMap[String, ActorRef] = TrieMap.empty
  private val cache: TrieMap[String, Any] = TrieMap.empty

  def subscribe(name: String, receiver: ActorRef): Option[ActorRef] =
    subscribers.putIfAbsent(name, receiver)

  def cache(name: String, result: Any): Option[Any] =
    cache.put(name, result)
}
*/