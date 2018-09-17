package controllers.vaadin

import javax.inject.Inject
import play.api.mvc._

class VaadinFavIconController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def favIcon = cc.actionBuilder.apply {
    Results.NotFound
  }
}
