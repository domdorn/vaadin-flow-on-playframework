package org.vaadin.playintegration

import java.io.InputStream
import java.net.URL
import java.util
import java.util._

import com.vaadin.flow.component.Component
import com.vaadin.flow.server._
import com.vaadin.flow.server.communication._
import com.vaadin.flow.server.startup.{AbstractRouteRegistryInitializer, RouteRegistry}
import com.vaadin.flow.server.webjar.WebJarServer
import com.vaadin.flow.theme.AbstractTheme

import scala.collection.JavaConverters

class VaadinPlayService(
                         contextRoot: String,
                         routes: => Seq[Class[_]],
                         deploymentConfiguration: PlayDeploymentConfiguration,
                         classloader: ClassLoader,
                         webJarServer: WebJarServer
                       )
  extends VaadinService(deploymentConfiguration) {

  private val routeRegistry = createRouteRegistry()

  private val contextResolver = new ServiceContextUriResolver
  setClassLoader(classloader)

  //  if (getClassLoader == null) {
  //    setClassLoader(Play.classloader)
  //    setClassLoader(classOf[VaadinPlayService].getClassLoader)
  //  }

  //  override def getStaticFileLocation(request: VaadinRequest): String = deploymentConfiguration.getStaticFileLocation

  //  override def getConfiguredWidgetset(request: VaadinRequest): String = deploymentConfiguration.getWidgetset

  //  override def getConfiguredTheme(request: VaadinRequest): String = Constants.DEFAULT_THEME_NAME

  //    override def isStandalone(request: VaadinRequest): Boolean = deploymentConfiguration.isStandalone

  override def getMimeType(resourceName: String): String = null // TODO use fileMimeTypes here

  //  override def getBaseDirectory: File = null // TODO

  override def requestCanCreateSession(request: VaadinRequest): Boolean =
  //    ServletUIInitHandler.isUIInitRequest(request) || isOtherRequest(request)
    true // TODO implement this

  private[this] def isOtherRequest(request: VaadinRequest) = false // TODO implement this

  //    !ServletPortletHelper.isAppRequest(request) &&
  //      !ServletUIInitHandler.isUIInitRequest(request) &&
  //      !ServletPortletHelper.isFileUploadRequest(request) &&
  //      !ServletPortletHelper.isHeartbeatRequest(request) &&
  //      !ServletPortletHelper.isPublishedFileRequest(request) &&
  //      !ServletPortletHelper.isUIDLRequest(request) &&
  //      !ServletPortletHelper.isPushRequest(request)

  override def getServiceName: String = deploymentConfiguration.name


  //  override def getMainDivId(session: VaadinSession, request: VaadinRequest, uiClass: Class[_ <: UI]): String = {
  ////     expecting that getPath starts always with /, substring(1) removes it
  //    val appId = deploymentConfiguration.getPath.substring(1) match {
  //      case "" => "ROOT"
  //      case p => p
  //    }
  //    val hashCode = appId.replaceAll("[^a-zA-Z0-9]", "").hashCode
  //
  //    appId + "-" + hashCode.abs
  //  }

  //  override def getThemeResourceAsStream(uI: UI, themeName: String, resource: String) = ???

  override def createRequestHandlers: java.util.List[RequestHandler] = {
    val handlers: util.List[RequestHandler] = new util.ArrayList[RequestHandler]
    handlers.add(0, new PlayBootstrapHandler(contextRoot))
    handlers.add(new VaadinPlayFaviconHandler)
    handlers.add(new SessionRequestHandler)
    handlers.add(new HeartbeatHandler)
    handlers.add(new UidlRequestHandler)
    handlers.add(new UnsupportedBrowserHandler)
    handlers.add(new StreamRequestHandler)

    //    val handlers = super.createRequestHandlers

    //    handlers.add(new ServletUIInitHandler)
    handlers
  }

  override def getRouteRegistry: RouteRegistry = routeRegistry

  def createRouteRegistry(): RouteRegistry = {
    // RouteRegistry.getInstance(getServlet.getServletContext)

    val registry = new PlayRouteRegistry()

    val routeRegistryInitializer = new AbstractRouteRegistryInitializer {
      def load(classes: stream.Stream[Class[_]]) = {
        validateRouteClasses(classes)
      }
    }
    val routesList = JavaConverters.asJavaCollection(routes)

    val classesStream: stream.Stream[Class[_]] = routesList.stream
    val foundRoutes = routeRegistryInitializer.load(classesStream)
    if (foundRoutes.isEmpty) {
      registry.setNavigationTargets(new util.TreeSet[Class[_ <: Component]]())
    } else {
      registry.setNavigationTargets(foundRoutes)
    }
    registry
  }

  override def getMainDivId(session: VaadinSession, request: VaadinRequest): String = {
    var appId: String = contextRoot

    if (appId == null || "" == appId || "/" == appId) appId = "ROOT"

    appId = appId.replaceAll("[^a-zA-Z0-9]", "")
    // Add hashCode to the end, so that it is still (sort of)
    // predictable, but indicates that it should not be used in CSS
    // and
    // such:
    var hashCode = appId.hashCode
    if (hashCode < 0) hashCode = -hashCode
    appId = appId + "-" + hashCode
    return appId
  }

  override def getStaticResource(url: String): URL = {
    println("def getStaticResource(url: String): URL")
    //    try
    //      return getServlet.getServletContext.getResource(path)
    //    catch {
    //      case e: MalformedURLException =>
    //        getLogger.warn("Error finding resource for '{}'", path, e)
    //    }
    //
    return null
  }

  /**
    * Finds the given resource in the servlet context or in a webjar.
    *
    * @param path
    * the path inside servlet context, automatically translated as
    * needed for webjars
    * @return a URL for the resource or <code>null</code> if no resource was
    *         found
    */
  private def getResourceClassPathOrWebJar(path: String): URL = {
    val bowerComponentsStatic = "/frontend/bower_components/"
    if (path.startsWith(bowerComponentsStatic)) {
      val webjarResourceUrl = VaadinResources.getWebjarResourceAsURL(path.substring(bowerComponentsStatic.length))

      return webjarResourceUrl.orNull
    }

    if (path.startsWith("/frontend/theme/")) {
      val themedResource = Option(this.classloader.getResource(path.substring(1)))
      return themedResource.orNull
    }
    println("getResourceInServletContextOrWebJar(path: String): URL")
    //    val servletContext = getServlet.getServletContext
    //    try {
    //      val url = servletContext.getResource(path)
    //      if (url != null) return url
    //      val webJarPath = getWebJarPath(path)
    //      if (webJarPath.isPresent) return servletContext.getResource(webJarPath.get)
    //    } catch {
    //      case e: MalformedURLException =>
    //        getLogger.warn("Error finding resource for '{}'", path, e)
    //    }
    null
  }

  /**
    * Opens a stream for the given resource found in the servlet context or in
    * a webjar.
    *
    * @param path
    * the path inside servlet context, automatically translated as
    * needed for webjars
    * @return a URL for the resource or <code>null</code> if no resource was
    *         found
    */
  private def getResourceInClasspathOrWebJarAsStream(path: String): Option[InputStream] = {
    val stream = Option(this.getClass.getClassLoader.getResourceAsStream(path.substring(1)))
    if (stream.isDefined) {
      return stream
    }
    import scala.compat.java8.OptionConverters

    val webJarPath: Option[String] = OptionConverters.toScala(getWebJarPath(path))
    return webJarPath.flatMap(wjp => {
      val finalWebJarPath: String = "META-INF/resources" + wjp
      val newStream: Option[InputStream] = Option(this.getClass.getClassLoader.getResourceAsStream(finalWebJarPath))

      return newStream.orElse({
        val webComponentsPath: String = finalWebJarPath.replace("webjars", "webjars/@webcomponents")
        Option(this.getClass.getClassLoader.getResourceAsStream(webComponentsPath))
      })
    }).orElse({
      Option(this.getClass.getClassLoader.getResourceAsStream("META-INF/resources" + path))
    })
  }


  private def getWebJarPath(path: String) = webJarServer.getWebJarResourcePath(path)

  /**
    * Resolves the given {@code url} resource and tries to find a themed or raw
    * version.
    * <p>
    * The themed version is always tried first, with the raw version used as a
    * fallback.
    *
    * @param url
    * the untranslated URL to the resource to find
    * @param browser
    * the web browser to resolve for, relevant for es5 vs es6
    * resolving
    * @param theme
    * the theme to use for resolving, or <code>null</code> to not
    * use a theme
    * @return the path to the themed resource if such exists, otherwise the
    *         resolved raw path
    */
  private def getThemedOrRawPath(url: String, browser: WebBrowser, theme: AbstractTheme): String = {
    val resourcePath = resolveResource(url, browser)
    val themeResourcePath = getThemeResourcePath(resourcePath, theme)
    if (themeResourcePath.isPresent) {
      val themeResource = getResourceClassPathOrWebJar(themeResourcePath.get)
      if (themeResource != null) return themeResourcePath.get
    }
    resourcePath
  }

  /**
    * Gets the theme specific path for the given resource.
    *
    * @param path
    * the raw path
    * @param theme
    * the theme to use for resolving, possibly <code>null</code>
    * @return the path to the themed version or an empty optional if no themed
    *         version could be determined
    */
  private def getThemeResourcePath(path: String, theme: AbstractTheme): Optional[String] = {
    if (theme == null) return Optional.empty[String]
    val themeUrl = theme.translateUrl(path)
    if (path == themeUrl) return Optional.empty[String]
    Optional.of(themeUrl)
  }


  override def getResource(url: String, browser: WebBrowser, theme: AbstractTheme): URL = getResourceClassPathOrWebJar(getThemedOrRawPath(url, browser, theme))

  override def getResourceAsStream(url: String, browser: WebBrowser, theme: AbstractTheme): InputStream = getResourceInClasspathOrWebJarAsStream(getThemedOrRawPath(url, browser, theme)).orNull

  override def resolveResource(url: String, browser: WebBrowser): String = {
    Objects.requireNonNull(url, "Url cannot be null")
    Objects.requireNonNull(browser, "Browser cannot be null")

    var frontendRootUrl: String = null
    val config = getDeploymentConfiguration
    if (browser.isEs6Supported) frontendRootUrl = config.getEs6FrontendPrefix
    else frontendRootUrl = config.getEs5FrontendPrefix

    contextResolver.resolveVaadinUri(url, frontendRootUrl)
  }

  override def getThemedUrl(url: String, browser: WebBrowser, theme: AbstractTheme): Optional[String] = {
    if (theme != null && !(resolveResource(url, browser) == getThemedOrRawPath(url, browser, theme))) {
      Optional.of(theme.translateUrl(url))
    } else {
      Optional.empty[String]
    }
  }
}