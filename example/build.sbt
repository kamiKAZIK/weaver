name := "example"
organization := "com.weaver"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.4.0" % "provided"
)