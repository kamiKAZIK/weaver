import sbt._

object DependencyVersionProvider extends AutoPlugin {
  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    lazy val versions = settingKey[VersionRegistry]("Bundles dependency versions")
  }
}
