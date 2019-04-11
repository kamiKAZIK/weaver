name := "rest-api"

versions := WeaverVersionRegistry

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % versions.value.get(artifactId = "argonaut")
)
