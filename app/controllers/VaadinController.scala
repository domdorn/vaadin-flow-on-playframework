package controllers

import akka.stream.scaladsl.StreamConverters
import com.typesafe.config.Config
import com.vaadin.flow.internal.CurrentInstance
import com.vaadin.flow.server.webjar.WebJarServer
import javax.inject.Inject
import org.vaadin.playintegration.{PlayDeploymentConfiguration, VaadinPlayRequestHandler, VaadinResources}
import play.api.Environment
import play.api.cache.SyncCacheApi
import play.api.http.{FileMimeTypes, HttpEntity}
import play.api.mvc._

class VaadinController @Inject()(
                                  configuration: Config,
                                  cache: SyncCacheApi,
                                  environment: Environment,
                                  mimeTypes: FileMimeTypes,
                                  cc: ControllerComponents) extends AbstractController(cc) {
  val defaultChunkSize = 8192

  CurrentInstance.clearAll()

  val deploymentConfiguration = new PlayDeploymentConfiguration("test", true, configuration)
  val webJarServer = new WebJarServer(deploymentConfiguration)
  val vaadinPlayRequestHandler = new VaadinPlayRequestHandler(deploymentConfiguration, cache, environment.classLoader, webJarServer)

  CurrentInstance.clearAll()

  def staticResource(path: String): Action[AnyContent] = cc.actionBuilder.apply { request =>
    val resource = this.getClass.getClassLoader.getResourceAsStream("META-INF/resources/VAADIN/static/" + path)
    val mimetype = mimeTypes.forFileName(path)

    Result(
      header = ResponseHeader(200),
      body = HttpEntity.Streamed(
        StreamConverters.fromInputStream(() => resource, defaultChunkSize),
        None,
        mimetype
      )
    )
  }


  def webJarResource(path: String): Action[AnyContent] = cc.actionBuilder.apply { request =>
    var resource = VaadinResources.getWebjarResourceAsStream(path)
    val response = resource.map(stream => {
      val mimetype = mimeTypes.forFileName(path)
      Result(
        header = ResponseHeader(200),
        body = HttpEntity.Streamed(
          StreamConverters.fromInputStream(() => stream, defaultChunkSize),
          None,
          mimetype
        )
      )

    }).getOrElse({
      Results.NotFound("resource with path " + path + " not found")
    })

    response
  }

  def getFE(path: String): Action[AnyContent] = cc.actionBuilder.apply { request =>
    val resource = Option(this.getClass.getClassLoader.getResourceAsStream("frontend/" + path))
        .getOrElse(this.getClass.getClassLoader.getResourceAsStream("META-INF/resources/frontend/" + path))

    val mimetype = mimeTypes.forFileName(path)
    Result(
      header = ResponseHeader(200),
      body = HttpEntity.Streamed(
        StreamConverters.fromInputStream(() => resource, defaultChunkSize),
        None,
        mimetype
      )
    )

  }

  def serveUI(path: String) = cc.actionBuilder.apply { request =>
    vaadinPlayRequestHandler.handleRequest(request)
  }

  def favIcon = cc.actionBuilder.apply {
    Results.NotFound
  }
}
