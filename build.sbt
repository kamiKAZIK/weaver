name := "weaver"
organization := "com.weaver"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.12"

publishArtifact := false

lazy val root = Project("weaver", file("."))
  .aggregate(executionApi, server, example)

lazy val executionApi = Project("execution-api", file("execution-api"))

lazy val restApi = Project("rest-api", file("rest-api"))

lazy val server = Project("server", file("server"))
  .dependsOn(executionApi, restApi)

lazy val example = Project("example", file("example"))
  .dependsOn(executionApi)
