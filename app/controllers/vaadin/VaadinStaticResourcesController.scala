package controllers.vaadin

import akka.stream.scaladsl.StreamConverters
import javax.inject.Inject
import play.api.http.HttpEntity
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
