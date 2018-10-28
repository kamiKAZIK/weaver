package com.kami.weaver.server.util

import java.nio.file.Paths

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigurationProvider {
  def resolve(configFileName: Option[String]): Config = {
    val defaultConfig = ConfigFactory.load
    configFileName.fold(defaultConfig.resolve)(fileName =>
      ConfigFactory.parseFile(Paths.get(fileName).toFile)
        .withFallback(defaultConfig)
        .resolve
    )
  }
}
