object VersionRegistry extends VersionRegistry

sealed trait VersionRegistry {
  lazy val scalaVersion = "2.11.12"

  lazy val argonaut = "6.2.2"

  lazy val akkaHttpArgonaut = "1.22.0"
  lazy val akkaHttp = "10.1.5"
  lazy val akka = "2.5.16"

  lazy val slick = "3.2.3"

  lazy val sparkSql = "2.4.0"

  lazy val log4j = "2.11.1"

  lazy val h2 = "1.4.197"
}
