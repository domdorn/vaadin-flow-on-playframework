package controllers.vaadin

import com.typesafe.config.Config
import com.vaadin.flow.internal.CurrentInstance
import com.vaadin.flow.server.webjar.WebJarServer
import javax.inject.Inject
import org.vaadin.playintegration.{PlayDeploymentConfiguration, VaadinPlayRequestHandler}
import play.api.Environment
import play.api.cache.SyncCacheApi
import play.api.http.FileMimeTypes
import play.api.mvc._

class VaadinFavIconController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def favIcon = cc.actionBuilder.apply {
    Results.NotFound
  }
}
