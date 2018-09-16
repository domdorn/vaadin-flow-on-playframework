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

class VaadinFrontendResourcesController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val defaultChunkSize = 8192

  def frontendResource(path: String): Action[AnyContent] = cc.actionBuilder.apply { request =>
    val resource = Option(this.getClass.getClassLoader.getResourceAsStream("frontend/" + path))
      .getOrElse(this.getClass.getClassLoader.getResourceAsStream("META-INF/resources/frontend/" + path))

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
