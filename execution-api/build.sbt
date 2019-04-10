name := "execution-api"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % versions.value.akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % versions.value.akka,
  "org.apache.spark" %% "spark-sql" % versions.value.sparkSql % Provided
)
