name := "server"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % versions.value.slick,
  "com.typesafe.slick" %% "slick-hikaricp" % versions.value.slick,
  "org.apache.logging.log4j" % "log4j-api" % versions.value.log4j,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % versions.value.log4j,
  "io.argonaut" %% "argonaut" % versions.value.argonaut,
  "de.heikoseeberger" %% "akka-http-argonaut" % versions.value.akkaHttpArgonaut,
  "org.apache.spark" %% "spark-sql" % versions.value.sparkSql,
  "com.h2database" % "h2" % versions.value.h2 % Runtime
)
