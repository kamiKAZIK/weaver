
name := "server"
organization := "com.kami.weaver"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-stream" % "2.5.16",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.1",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.11.1",
  "io.argonaut" %% "argonaut" % "6.2.2",
  "de.heikoseeberger" %% "akka-http-argonaut" % "1.22.0",
  "com.h2database" % "h2" % "1.4.197" % Runtime,
  "org.apache.spark" %% "spark-sql" % "2.3.0"
)
