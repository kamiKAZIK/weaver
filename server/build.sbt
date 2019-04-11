name := "server"

versions := WeaverVersionRegistry

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % versions.value.get(artifactId = "slick"),
  "com.typesafe.slick" %% "slick-hikaricp" % versions.value.get(artifactId = "slick"),
  "org.apache.logging.log4j" % "log4j-api" % versions.value.get(artifactId = "log4j"),
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % versions.value.get(artifactId = "log4j"),
  "io.argonaut" %% "argonaut" % versions.value.get(artifactId = "argonaut"),
  "de.heikoseeberger" %% "akka-http-argonaut" % versions.value.get(artifactId = "akka-http-argonaut"),
  "org.apache.spark" %% "spark-sql" % versions.value.get(artifactId = "spark-sql"),
  "com.h2database" % "h2" % versions.value.get(artifactId = "h2") % Runtime
)
