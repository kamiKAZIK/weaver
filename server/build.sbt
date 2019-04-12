name := "server"

versions := WeaverVersionRegistry

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick",
  "com.typesafe.slick" %% "slick-hikaricp"
).map(_ % versions.value.get(artifactId = "slick"))

libraryDependencies ++= Seq(
  "org.apache.logging.log4j" % "log4j-api",
  "org.apache.logging.log4j" % "log4j-slf4j-impl"
).map(_ % versions.value.get(artifactId = "log4j"))

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-java8"
).map(_ % versions.value.get(artifactId = "circe"))

libraryDependencies ++= Seq(
  "de.heikoseeberger" %% "akka-http-circe" % versions.value.get(artifactId = "akka-http-circe"),
  "org.apache.spark" %% "spark-sql" % versions.value.get(artifactId = "spark-sql"),
  "com.h2database" % "h2" % versions.value.get(artifactId = "h2") % Runtime
)
