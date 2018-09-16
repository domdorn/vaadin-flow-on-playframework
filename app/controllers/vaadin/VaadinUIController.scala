package controllers.vaadin

import com.typesafe.config.Config
import com.vaadin.flow.internal.CurrentInstance
import com.vaadin.flow.server.webjar.WebJarServer
import javax.inject.Inject
import org.vaadin.playintegration.{PlayDeploymentConfiguration, VaadinPlayRequestHandler, VaadinResources}
import play.api.Environment
import play.api.cache.SyncCacheApi
import play.api.http.{FileMimeTypes, HttpEntity}
import play.api.mvc._

class VaadinUIController @Inject()(
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

  def serveUI(path: String) = cc.actionBuilder.apply { request =>
    vaadinPlayRequestHandler.handleRequest(request)
  }

}
