package org.vaadin.playintegration

import com.vaadin.flow.server.startup.RouteRegistry

class PlayRouteRegistry extends RouteRegistry() {
  println("created PlayRoureRegistry")
  println(getRegisteredRoutes)
}
