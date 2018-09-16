package controllers.vaadin

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

class VaadinStaticResourcesController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val defaultChunkSize = 8192

  def staticResource(path: String): Action[AnyContent] = cc.actionBuilder.apply { request =>
    val resource = this.getClass.getClassLoader.getResourceAsStream("META-INF/resources/VAADIN/static/" + path)
    val mimetype = cc.fileMimeTypes.forFileName(path)

    Result(
      header = ResponseHeader(200),
      body = HttpEntity.Streamed(
        StreamConverters.fromInputStream(() => resource, defaultChunkSize),
        None,
        mimetype
      )
    )
  }

}
