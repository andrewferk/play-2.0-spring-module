package play.api.modules.spring

import org.springframework.context.ApplicationContext


/**
 *
 * @author wsargent
 * @since 12/10/11
 */

object Spring {

  def applicationContext : Option[ApplicationContext] = SpringPlugin.applicationContext

  def getBean(name:String) = {
    applicationContext match {
      case None => throw new SpringException()
      case Some(s) => s.getBean(name)
    }
  }

  def getBeanOfType[T](pType:Class[T]) : T = {
    val beans = getBeansOfType[T](pType)
    val (key, value) = beans.head
    value
  }

  def getBeansOfType[T](clazz:Class[T]) : Map[String, T] = {
    applicationContext match {
      case None => throw new SpringException()
      case Some(s) => s.getBeansOfType(clazz).asInstanceOf[Map[String, T]]
    }
  }
}