package org.vaadin.playintegration

import java.io._
import java.security.Principal
import java.util.{Collections, Locale, UUID}

import com.vaadin.flow.server.{VaadinRequest, VaadinService, WrappedSession}
import play.api.http.HeaderNames
import play.api.mvc.{AnyContent, Request}

import scala.collection.JavaConverters._


class VaadinPlayRequest(
                         request: Request[AnyContent],
                         vaadinService: VaadinPlayService,
                         contextPath: String,
                         session: VaadinPlaySession)
  extends VaadinRequest {

  private lazy val textInputStreamOption = {

    request.body
      .asJson.map(j => j.toString())
      .orElse(request.body.asText)
      .map(s => new ByteArrayInputStream(s.getBytes()))

  }
  private lazy val fileInputStreamOption = request.body.asMultipartFormData map { d =>
    d.files.headOption.map { a =>
      // FIXME: this is far from an optimal solution
      val file = File.createTempFile(UUID.randomUUID.toString, ".tmp")
      a.ref.moveTo(file, true)
      new FileInputStream(file)
    }.orNull
  }

  private[this] lazy val inputStream: InputStream = textInputStreamOption getOrElse fileInputStreamOption.orNull

  override def getParameter(parameter: String): String = request.queryString.get(parameter) match {
    case Some(values) => values.head
    case None => null
  }

  override def getParameterMap: java.util.Map[String, Array[String]] =
    request.queryString.map(a => (a._1, a._2.toArray)).asJava

  override def getContentLength: Int = Integer.parseInt(request.headers.get(HeaderNames.CONTENT_LENGTH).get)

  override def getInputStream: InputStream = inputStream

  override def getAttribute(name: String): AnyRef = getWrappedSession.getAttribute(name)

  override def setAttribute(name: String, value: Any) {
    getWrappedSession.setAttribute(name, value)
  }

  override def getPathInfo: String = request.path.substring(getContextPath.length)

  override def getContextPath: String = contextPath

  override def getWrappedSession: WrappedSession = getWrappedSession(true)

  override def getWrappedSession(allowSessionCreation: Boolean): WrappedSession = {
    val s = if (!session.isNew) new WrappedPlaySession(session)
    else if (session.isNew && allowSessionCreation) new WrappedPlaySession(session)
    else null


    s
  }

  override def getContentType: String = request.contentType.orNull

  override def getLocale: Locale = request.acceptLanguages.headOption.map(_.locale).orNull

  override def getRemoteAddr: String = request.remoteAddress

  override def isSecure: Boolean = request.secure

  override def getHeader(headerName: String): String = request.headers.get(headerName).orNull

  override def getService: VaadinService = vaadinService

  override def getCookies: Array[javax.servlet.http.Cookie] = null

  override def getAuthType: String = ""

  override def getRemoteUser: String = ""

  override def getUserPrincipal: Principal = null

  override def isUserInRole(role: String): Boolean = false

  override def removeAttribute(name: String) {
    getWrappedSession.removeAttribute(name)
  }

  override def getAttributeNames: java.util.Enumeration[String] = null

  override def getLocales: java.util.Enumeration[Locale] = Collections.enumeration(request.acceptLanguages.map(_.locale).asJavaCollection)

  override def getRemoteHost: String = request.remoteAddress

  override def getRemotePort: Int = -1

  override def getCharacterEncoding: String = ""

  override def getReader: BufferedReader = new BufferedReader(new InputStreamReader(inputStream))

  override def getMethod: String = request.method

  override def getDateHeader(name: String): Long = 0L

  override def getHeaderNames: java.util.Enumeration[String] =
    request.headers.keys.iterator.asJavaEnumeration

  override def getHeaders(name: String): java.util.Enumeration[String] =
    request.headers.getAll(name).iterator.asJavaEnumeration
}