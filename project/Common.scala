import sbt.Keys._
import sbt._

object Common {
  lazy val settings: Seq[Setting[_]] = Seq(
    organization := "com.pointlogic",
    version := "0.1-SNAPSHOT",
    scalaVersion := VersionRegistry.scalaVersion,
  )
}
