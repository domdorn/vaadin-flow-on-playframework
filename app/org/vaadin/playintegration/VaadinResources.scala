package org.vaadin.playintegration

import java.io.InputStream
import java.net.URL

object VaadinResources {
  private val loader: ClassLoader = this.getClass.getClassLoader

  def getWebjarResourceAsStream(path: String): Option[InputStream] = {
    val mainWebJarPath = "META-INF/resources/webjars/" + path
    val webComponentsWebJarPath = "META-INF/resources/webjars/@webcomponents/" + path
    val mainWebJarStream = loader.getResourceAsStream(mainWebJarPath)
    val webComponentsWebJarStream = loader.getResourceAsStream(webComponentsWebJarPath)
    Option(mainWebJarStream)
      .orElse(Option(webComponentsWebJarStream))
  }

  def getWebjarResourceAsURL(path: String): Option[URL] = {
    Option(loader.getResource("META-INF/resources/webjars/" + path))
      .orElse(Option(loader.getResource("META-INF/resources/webjars/@webcomponents/" + path)))
  }
}
