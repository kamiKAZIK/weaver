import scala.collection.concurrent.TrieMap

trait VersionRegistry {
  def get(artifactId: String): String = VersionRegistry.registry(artifactId)
  def add(artifactId: String, version: String): Unit = VersionRegistry.registry.putIfAbsent(artifactId, version)
}

object VersionRegistry {
  private val registry: TrieMap[String, String] = TrieMap.empty
}
