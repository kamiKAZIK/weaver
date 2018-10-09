name := "weaver"
organization := "com.kami.weaver"

version := "0.1"

scalaVersion := "2.11.12"

publishArtifact := false

lazy val root = Project("weaver", file("."))
  .aggregate(jobApi, server)

lazy val jobApi = Project("job-api", file("job-api"))

lazy val restApi = Project("rest-api", file("rest-api"))

lazy val server = Project("server", file("server"))
  .dependsOn(jobApi, restApi)
