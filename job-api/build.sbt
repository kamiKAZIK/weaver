name := "job-api"
organization := "com.kami.weaver"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-stream" % "2.5.16",
  "org.apache.spark" %% "spark-sql" % "2.3.0" % "provided"
)
