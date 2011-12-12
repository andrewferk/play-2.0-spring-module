package play.api.modules.spring

import java.net.URL
import org.springframework.context.support.GenericApplicationContext
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.xml.sax.InputSource
import org.springframework.beans.factory.BeanCreationException
import java.io.{File, IOException, InputStream}

import play.api._

// Imports the implicit "app" Application for Play.classloader
import play.api.Play._

/**
 * This is a port of the Java Spring plugin.  It looks for an application-context.xml file in the classpath,
 * followed by conf/application-context.xml.  It does not do real time watching or reloading of the
 * application context file.
 *
 * @author wsargent
 * @since 12/10/11
 */
object SpringPlugin
{
  val PLAY_SPRING_COMPONENT_SCAN_FLAG = "play.spring.component-scan";
  val PLAY_SPRING_COMPONENT_SCAN_BASE_PACKAGES = "play.spring.component-scan.base-packages";
  val PLAY_SPRING_ADD_PLAY_PROPERTIES = "play.spring.add-play-properties";
  val PLAY_SPRING_NAMESPACE_AWARE = "play.spring.namespace-aware";

  // XXX Is this thread safe???
  var applicationContext: Option[GenericApplicationContext] = None
}

class SpringPlugin(app: Application) extends Plugin
{
  import SpringPlugin._

  private var startDate: Long = 0;

  override def onStop {
    applicationContext.map {
      context =>
        Logger.debug("Closing Spring application context")
        context.close()
    }
  }

  override def onStart {
    var url: URL = Play.classloader.getResource(Play.isDev + ".application-context.xml")
    if (url == null) {
      url = Play.classloader.getResource("application-context.xml")
    }

    // This is a last ditch fallback since it looks like conf isn't on the classpath...
    if (url == null) {
      val dir = Play.application.path + "/conf"
      url = new File(dir, "application-context.xml").toURI.toURL
    }

    // Make the failure case more explicit
    if (url == null) {
      throw new play.api.PlayException("No application-context.xml!", "Please include an application-context.xml file in your classpath");
    }

    var is: InputStream = null
    try {
      Logger.debug("Starting Spring application context")
      applicationContext = Some(new GenericApplicationContext)
      applicationContext.map {
        context =>
          context.setClassLoader(Play.classloader)
          val xmlReader: XmlBeanDefinitionReader = new XmlBeanDefinitionReader(context)
          if (Play.configuration.getBoolean(PLAY_SPRING_NAMESPACE_AWARE).getOrElse(false)) {
            xmlReader.setNamespaceAware(true)
          }
          xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE)
          if (Play.configuration.getBoolean(SpringPlugin.PLAY_SPRING_ADD_PLAY_PROPERTIES).getOrElse(false)) {
            Logger.debug("Adding PropertyPlaceholderConfigurer with Play properties")
            val configurer: PropertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer
            val props = new java.util.Properties()
            Play.configuration.keys.foreach {
              key =>
                val value = Play.configuration.full(key)
                props.setProperty(key, value)
            }
            configurer.setProperties(props)
            context.addBeanFactoryPostProcessor(configurer)
          }
          else {
            Logger.debug("PropertyPlaceholderConfigurer with Play properties NOT added")
          }
          val doComponentScan: Boolean = Play.configuration.getBoolean(PLAY_SPRING_COMPONENT_SCAN_FLAG).getOrElse(true)
          Logger.debug("Spring configuration do component scan: " + doComponentScan)
          if (doComponentScan) {
            val scanner: ClassPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(context)
            val scanBasePackage: String = (Play.configuration.get(PLAY_SPRING_COMPONENT_SCAN_BASE_PACKAGES)).map(f => f.value).getOrElse("")
            Logger.debug("Base package for scan: " + scanBasePackage)
            Logger.debug("Scanning...")
            scanner.scan(scanBasePackage.split(","): _*)
            Logger.debug("... component scanning complete")
          }
          is = url.openStream
          xmlReader.loadBeanDefinitions(new InputSource(is))
          var originalClassLoader: ClassLoader = Thread.currentThread.getContextClassLoader
          Thread.currentThread.setContextClassLoader(Play.classloader)
          try {
            context.refresh()
            startDate = System.currentTimeMillis
          }
          catch {
            case e: BeanCreationException => {
              var ex: Throwable = e.getCause
              if (ex.isInstanceOf[PlayException]) {
                throw ex.asInstanceOf[PlayException]
              }
              else {
                throw e
              }
            }
          }
          finally {
            Thread.currentThread.setContextClassLoader(originalClassLoader)
          }
      }
    }
    catch {
      case e: IOException => {
        Logger.error("Can't load spring config file", e)
      }
    }
    finally {
      if (is != null) {
        try {
          is.close()
        }
        catch {
          case e: IOException => {
            Logger.error("Can't close spring config file stream", e)
          }
        }
      }
    }
  }

}