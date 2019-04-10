lazy val root = Project("weaver", file("."))
  .settings(Common.settings)
  .settings(
    name := "weaver",
    publishArtifact := false
  )
  .aggregate(executionApi, server)

lazy val executionApi = Project("execution-api", file("execution-api"))
  .settings(Common.settings)

lazy val restApi = Project("rest-api", file("rest-api"))
  .settings(Common.settings)

lazy val server = project.in(file("server"))
  .settings(Common.settings)
  .dependsOn(executionApi, restApi)
