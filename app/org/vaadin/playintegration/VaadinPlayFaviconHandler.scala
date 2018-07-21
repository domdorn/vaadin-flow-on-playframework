package org.vaadin.playintegration

import com.vaadin.flow.server.{RequestHandler, VaadinRequest, VaadinResponse, VaadinSession}

class VaadinPlayFaviconHandler extends RequestHandler{
  override def handleRequest(session: VaadinSession, request: VaadinRequest, response: VaadinResponse): Boolean = {
    request match {
      case x : VaadinPlayRequest => if(request.getPathInfo.contains("favicon.ico")) {
        response.setStatus(404)
        true
      } else {
        false
      }
      case _ => false
    }
  }
}
