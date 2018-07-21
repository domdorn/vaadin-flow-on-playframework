package org.vaadin.playintegration

import java.util.{Properties, function}

import com.vaadin.flow.component.UI
import com.vaadin.flow.function.DeploymentConfiguration
import com.vaadin.flow.server.Constants
import com.vaadin.flow.shared.ApplicationConstants
import com.vaadin.flow.shared.communication.PushMode

import scala.util.Try
//import com.vaadin.shared.communication.PushMode
//import com.vaadin.server.DeploymentConfiguration.LegacyProperyToStringMode
//import com.vaadin.server.Constants

/**
  * @author Henri Kerola / Vaadin
  */
class PlayDeploymentConfiguration(
                                   val name: String,
                                   val isStandalone: Boolean,
                                   //                                   val isScaladin: Boolean,
                                   confInput: com.typesafe.config.Config)
  extends DeploymentConfiguration {

  val conf = confInput.getConfig("vaadin.myapp")

  lazy val getPath: String = conf.getString("path")
  //
  lazy val getUi: String = conf.getString("ui")
  //
  //  lazy val getWidgetset: String = conf.getString("widgetset").getOrElse(Constants.DEFAULT_WIDGETSET)
  lazy val getWidgetset: String = conf.getString("widgetset")
  //
  //  lazy val getSessionTimeout: Int = conf.getInt("session_timeout").getOrElse(1800)
  lazy val getSessionTimeout: Int = Try(conf.getInt("session_timeout")).getOrElse(1800)

  lazy val getStaticFileLocation: String = "" // TODO

  override lazy val isProductionMode: Boolean = /*Play.current.mode == Mode.Prod*/ false

  //  override lazy val isXsrfProtectionEnabled: Boolean = conf.getBoolean("xsrf_protection").getOrElse(true)
  override lazy val isXsrfProtectionEnabled: Boolean = true

  //  override lazy val getResourceCacheTime: Int = -1 // Not in use in

  //  override lazy val getHeartbeatInterval: Int = conf.getInt("heartbeat_interval").getOrElse(300)
  override lazy val getHeartbeatInterval: Int = Try(conf.getInt("heartbeat_interval")).getOrElse(300)

  //  override lazy val isCloseIdleSessions: Boolean = conf.getBoolean("close_idle_sessions").getOrElse(false)
  override lazy val isCloseIdleSessions: Boolean = Try(conf.getBoolean("close_idle_session")).getOrElse(false)

  override def getInitParameters: Properties = new Properties {
    //    if (isScaladin) {
    //      put("ScaladinUI", getUi)
    //    }
  }

//  override def getApplicationOrSystemProperty(propertyName: String, defaultValue: String): String =
  //    if (!isScaladin && propertyName == "UI")
  //      return getUi
  //    else
//    Try(conf.getString(propertyName)).getOrElse(defaultValue)

  override def getPushMode: PushMode = PushMode.DISABLED

  //  override def getLegacyPropertyToStringMode: LegacyProperyToStringMode = LegacyProperyToStringMode.DISABLED
  override def isRequestTiming: Boolean = true

  override def isSyncIdCheckEnabled: Boolean = true

  override def isSendUrlsAsParameters: Boolean = true

  override def getPushURL: String = null

  override def getApplicationOrSystemProperty[T](propertyName: String, defaultValue: T, converter: function.Function[String, T]): T = {
    Try(conf.getString(propertyName)).map(converter(_)).getOrElse(defaultValue)
  }

  override def getUIClassName: String = classOf[UI].getName

  override def getClassLoaderName: String = null // only used to load a specific classloader

//  override def getDevelopmentFrontendPrefix: String = ApplicationConstants.CONTEXT_PROTOCOL_PREFIX +
//    "public/" + "frontend/"

  override def getDevelopmentFrontendPrefix: String = Constants.FRONTEND_URL_DEV_DEFAULT
}