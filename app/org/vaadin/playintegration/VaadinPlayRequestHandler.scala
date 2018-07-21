package org.vaadin.playintegration

import java.io.ByteArrayOutputStream
import java.util.UUID

import akka.util.ByteString
import com.vaadin.flow.internal.CurrentInstance
import com.vaadin.flow.server.webjar.WebJarServer
import play.api.cache.SyncCacheApi
import play.api.http.HttpEntity.Strict
import play.api.mvc._

object VaadinPlayRequestHandler {

  def current: VaadinPlayRequestHandler = CurrentInstance.get(classOf[VaadinPlayRequestHandler])

  def current_=(current: VaadinPlayRequestHandler) {
    CurrentInstance.set(classOf[VaadinPlayRequestHandler], current)
  }
}

/**
  * @author Henri Kerola / Vaadin
  */
class VaadinPlayRequestHandler(
                                deploymentConfiguration: PlayDeploymentConfiguration,
                                cache: SyncCacheApi,
                                classLoader: ClassLoader,
                                webJarServer: WebJarServer
                              ) {

  CurrentInstance.clearAll()
  VaadinPlayRequestHandler.current = this

  val service: VaadinPlayService = createVaadinPlayService(deploymentConfiguration, webJarServer)
  service.init()
  service.setCurrentInstances(null, null)
  playPluginInitialized()

  CurrentInstance.clearAll()

  protected def playPluginInitialized() {
    // Empty by default
  }

  def handleRequest(request: Request[AnyContent]): Result = {
    val confPathWithSlash = deploymentConfiguration.getPath + "/"

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

  def doHandleRequest(request: Request[AnyContent]): (Session, VaadinPlayResponse) = {
    val sessionId = request.session.get("vaadinPlaySession") getOrElse UUID.randomUUID.toString
    val playSession = request.session + ("vaadinPlaySession" -> sessionId)

    val vaadinPlaySession = retrieveVaadinPlaySession(sessionId)
    vaadinPlaySession.onAfterRetrieve()

    CurrentInstance.clearAll()

    val vaadinRequest = createVaadinPlayRequest(request, vaadinPlaySession)
    val vaadinResponse = createVaadinPlayResponse()

    // TODO: ensureCookiesEnabled
    try {
      service.handleRequest(vaadinRequest, vaadinResponse)
    } finally {
      vaadinResponse.cleanUp()
      vaadinPlaySession.onBeforeStore()
      storeVaadinPlaySession(sessionId, vaadinPlaySession, deploymentConfiguration.getSessionTimeout)
    }

    (playSession, vaadinResponse)
  }

  protected def createVaadinPlayService(deploymentConfiguration: PlayDeploymentConfiguration, webJarServer: WebJarServer): VaadinPlayService =
    new VaadinPlayService(this, deploymentConfiguration, classLoader, webJarServer)

  protected def createVaadinPlayRequest(request: Request[AnyContent], session: VaadinPlaySession): VaadinPlayRequest =
    new VaadinPlayRequest(request, service, deploymentConfiguration.getPath, session)

  protected def createVaadinPlayResponse(): VaadinPlayResponse =
    new VaadinPlayResponse(service)

  import scala.concurrent.duration._
  protected def retrieveVaadinPlaySession(sessionId: String): VaadinPlaySession =
    cache.getOrElseUpdate(sessionId, 300 seconds){
      println(s"        couldn't find session with id ${sessionId} in cache, creating a new one")
      new DefaultVaadinPlaySession(sessionId)}.asInstanceOf[VaadinPlaySession]

//    cache.get(sessionId).getOrElse({
//
//      new DefaultVaadinPlaySession(sessionId)
//    }).asInstanceOf[VaadinPlaySession]


  protected def storeVaadinPlaySession(sessionId: String, vaadinPlaySession: VaadinPlaySession, sessionTimeout: Int) {
    //println("         storing vaadin play session with id " + sessionId)
//    cache.set(sessionId, vaadinPlaySession, sessionTimeout seconds)
    cache.set(sessionId, vaadinPlaySession)
  }

}