package spring

import java.net.URL
import org.springframework.context.support.GenericApplicationContext
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.xml.sax.InputSource
import org.springframework.beans.factory.BeanCreationException
import java.io.IOException

import play.api._

// Imports the implicit "app" Application for Play.classloader

import play.api.Play._

/**
 * This is a port of the Java Spring plugin.  It looks for an application context file in the classpath,
 * followed by conf/application-context.xml.  It does not do real time watching or reloading of the
 * application context file.
 *
 * @author wsargent
 * @since 12/10/11
 */
object SpringPlugin {

  /**
   * This property determines whether the play configuration properties are exposed as Spring properties.
   */
  val PLAY_SPRING_ADD_PLAY_PROPERTIES = "play.spring.add-play-properties"

  /**
   * This property determines whether the applicationContext file is namespace aware.  The default is true.
   */
  val PLAY_SPRING_NAMESPACE_AWARE = "play.spring.namespace-aware"

  /**
   * This property defines the context path for Spring.  Usually you would set this to "applicationContext.xml".
   */
  val PLAY_SPRING_CONTEXT_PATH = "play.spring.context-path"

  /**
   * If PLAY_SPRING_NAMESPACE_AWARE is not found, return this value.
   */
  val NAMESPACE_AWARE_DEFAULT = true

  /**
   * If PLAY_SPRING_ADD_PLAY_PROPERTIES is not found, return this value.
   */
  val ADD_PLAY_PROPERTIES_DEFAULT = false

  var applicationContext: Option[GenericApplicationContext] = None
}

class SpringPlugin(app: Application) extends Plugin {

  import SpringPlugin._

  private var startDate: Long = 0;

  override def enabled = applicationContext.isDefined

  override def onStop() {
    applicationContext.map {
      context =>
        Logger.debug("Closing Spring application context")
        context.close()
    }
  }

  override def onStart() {

    var url: URL = Play.configuration.getString(PLAY_SPRING_CONTEXT_PATH) match {
      case Some(contextPath) => {
        Logger.debug("Loading application context: " + contextPath);
        Play.classloader.getResource(contextPath)
      }
      case None => {
        Logger.debug("Loading default application context: application-context.ml");
        Play.classloader.getResource("application-context.xml")
      }
    }

    // Make the failure case more explicit
    if (url == null) {
      throw new play.api.PlayException("No application context found!!", "Please define play.spring.context-path to point to a file in your classpath");
    }

    try {
      Logger.debug("Starting Spring application context")
      applicationContext = Some(new GenericApplicationContext)
      applicationContext.map {
        context =>
          context.setClassLoader(Play.classloader)
          val xmlReader: XmlBeanDefinitionReader = new XmlBeanDefinitionReader(context)

          // See if the app should be namespace aware
          if (Play.configuration.getBoolean(PLAY_SPRING_NAMESPACE_AWARE).getOrElse(NAMESPACE_AWARE_DEFAULT)) {
            xmlReader.setNamespaceAware(true)
          }
          xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE)

          // Use the play properties...
          if (Play.configuration.getBoolean(SpringPlugin.PLAY_SPRING_ADD_PLAY_PROPERTIES).getOrElse(ADD_PLAY_PROPERTIES_DEFAULT)) {
            Logger.debug("Adding PropertyPlaceholderConfigurer with Play properties")
            val configurer: PropertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer
            val props = new java.util.Properties()
            Play.configuration.keys.foreach {
              key =>
                Play.configuration.getString(key).map {
                  props.setProperty(key, _)
                }
            }
            configurer.setProperties(props)
            context.addBeanFactoryPostProcessor(configurer)
          }
          else {
            Logger.debug("PropertyPlaceholderConfigurer with Play properties NOT added")
          }

          // Load in the input stream, and set up the context.
          val is = url.openStream
          try {
            xmlReader.loadBeanDefinitions(new InputSource(is))
            val originalClassLoader: ClassLoader = Thread.currentThread.getContextClassLoader
            Thread.currentThread.setContextClassLoader(Play.classloader)
            try {
              context.refresh()
              startDate = System.currentTimeMillis
            } catch {
              case e: BeanCreationException => {
                val ex: Throwable = e.getCause
                if (ex.isInstanceOf[PlayException]) {
                  throw ex.asInstanceOf[PlayException]
                } else {
                  throw e
                }
              }
            } finally {
              Thread.currentThread.setContextClassLoader(originalClassLoader)
            }
          } finally {
            is.close()
          }
      }
    } catch {
      case e: IOException => {
        Logger.error("Can't load spring config file", e)
      }
    }

  }

}