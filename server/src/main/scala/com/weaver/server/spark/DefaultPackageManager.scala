package com.weaver.server.spark
import java.util.concurrent.CopyOnWriteArraySet

case class DefaultPackageManager() extends PackageManager {
  override protected[this] def registry: CopyOnWriteArraySet[String] = DefaultPackageManager.registry
}

case object DefaultPackageManager {
  val registry: CopyOnWriteArraySet[String] = new CopyOnWriteArraySet
}

