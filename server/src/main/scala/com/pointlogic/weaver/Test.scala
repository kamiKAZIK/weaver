package com.pointlogic.weaver

import scala.reflect.runtime.universe
import java.net.{URL, URLClassLoader}

import com.kami.api.job.WeaverJob

object Test {
  def main(args: Array[String]): Unit = {
    val urls = Array(
      new URL("file:///home/ekazakas/.ivy2/local/com.example/example_2.11/0.1/jars/example_2.11.jar")
    )

    val runtimeMirror = universe.runtimeMirror(URLClassLoader.newInstance(urls, getClass.getClassLoader))
    val module = runtimeMirror.staticModule("com.example.TestJob")
    val obj = runtimeMirror.reflectModule(module)

    val job = obj.instance.asInstanceOf[WeaverJob]
    job.doIt


    //val obj = Class.forName("com.example.TestJob", true, URLClassLoader.newInstance(urls, getClass.getClassLoader))
    //println(obj)
  }
}
