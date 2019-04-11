name := "execution-api"

versions := WeaverVersionRegistry

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % versions.value.get(artifactId = "akka-http") % Provided,
  "com.typesafe.akka" %% "akka-stream" % versions.value.get(artifactId = "akka") % Provided,
  "org.apache.spark" %% "spark-sql" % versions.value.get(artifactId = "spark-sql") % Provided
)
