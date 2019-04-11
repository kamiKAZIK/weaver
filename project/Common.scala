import sbt.Keys._
import sbt._

object Common {
  lazy val settings: Seq[Setting[_]] = Seq(
    organization := "com.kami",
    version := "0.1-SNAPSHOT",
    scalaVersion := WeaverVersionRegistry.get("scala"),
  )
}
