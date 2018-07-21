package org.vaadin.playintegration

import scala.collection.mutable

class DefaultVaadinPlaySession(val sessionId: String) extends VaadinPlaySession {
  println(s"creating a DefaultVaadinPlaySession with id ${sessionId}")
  private[this] var newSession = true
  private[this] var _lastAccessedTime: Long = 0L

  private[this] val attributeMap = mutable.Map.empty[String, Any]

  override val creationTime: Long = {
    //    println(s"setting creationTime on ${this}")
    System.currentTimeMillis
  }

  override def lastAccessedTime: Long = _lastAccessedTime

  override var maxInactiveInterval: Int = 1800

  override def isNew: Boolean = newSession

  override def getAttribute(name: String): Option[Any] = attributeMap.get(name)

  override def setAttribute(name: String, value: Any) = attributeMap += (name -> value)

  override def removeAttribute(name: String) = attributeMap -= name

  override def attributeNames: Set[String] = attributeMap.keySet.toSet

  override def onBeforeStore() {
    newSession = false
    _lastAccessedTime = System.currentTimeMillis()
  }


}