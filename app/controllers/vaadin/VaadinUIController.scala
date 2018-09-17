package controllers.vaadin

import java.io.ByteArrayOutputStream
import java.util.UUID

import akka.util.ByteString
import com.typesafe.config.Config
import com.vaadin.flow.internal.CurrentInstance
import com.vaadin.flow.server.webjar.WebJarServer
import com.vaadin.starter.beveragebuddy.ui.views.categorieslist.CategoriesList
import com.vaadin.starter.beveragebuddy.ui.views.reviewslist.ReviewsList
import javax.inject.Inject
import org.vaadin.playintegration._
import play.api.Environment
import play.api.cache.SyncCacheApi
import play.api.http.FileMimeTypes
import play.api.http.HttpEntity.Strict
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

  CurrentInstance.clearAll()

  val service: VaadinPlayService = new VaadinPlayService(
    "/my-vaadin-app",
    {
      // TODO classpath scanning of classes with @Route or @RouteAlias,
      // see com.vaadin.flow.server.startup.RouteRegistryInitializer
      Seq(classOf[ReviewsList], classOf[CategoriesList])
    },
    deploymentConfiguration,
    environment.classLoader,
    webJarServer
  )
  service.init()
  service.setCurrentInstances(null, null)

  def serveUI(path: String) = cc.actionBuilder.apply { request =>
    handleRequest(request)
  }

  def handleRequest(request: Request[AnyContent]): Result = {
    val confPathWithSlash = deploymentConfiguration.getPath + "/" // TODO DODO make this dynamic

    if (!request.path.startsWith(confPathWithSlash)) {
      // requested path must end with slash
      Results.Redirect(confPathWithSlash, request.queryString)
    } else {

      val (playSession, response) = doHandleRequest(request)

      Result(
        ResponseHeader(response.statusCode, response.headersMap.filterNot(x => x._1 == "Content-Type").toMap),
        Strict(ByteString.apply(response.outputStream.asInstanceOf[ByteArrayOutputStream].toByteArray), response.headersMap.get("Content-Type"))
      ).withSession(playSession)
    }
  }

  import scala.concurrent.duration._

  def doHandleRequest(request: Request[AnyContent]): (Session, VaadinPlayResponse) = {
    val sessionId = request.session.get("vaadinPlaySession") getOrElse UUID.randomUUID.toString
    val playSession = request.session + ("vaadinPlaySession" -> sessionId)

    val vaadinPlaySession = cache.getOrElseUpdate(sessionId, 300 seconds) {new DefaultVaadinPlaySession(sessionId)}
      .asInstanceOf[VaadinPlaySession]

    vaadinPlaySession.onAfterRetrieve()

    CurrentInstance.clearAll()

    val vaadinRequest = new VaadinPlayRequest(request, service, deploymentConfiguration.getPath, vaadinPlaySession)
    val vaadinResponse = new VaadinPlayResponse(service)

    // TODO: ensureCookiesEnabled
    try {
      service.handleRequest(vaadinRequest, vaadinResponse)
    } finally {
      vaadinResponse.cleanUp()
      vaadinPlaySession.onBeforeStore()
      cache.set(sessionId, vaadinPlaySession, deploymentConfiguration.getSessionTimeout seconds)
    }

    (playSession, vaadinResponse)
  }


}
