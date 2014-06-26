package play.api.modules.spring

import play.api.UnexpectedException

/**
 *
 * @author wsargent
 * @since 12/10/11
 */

//class SpringException(title: String = "Spring context is not started!",
//                     description: String = "The Spring application context is not started.",
//                      cause: Option[Throwable] = None) extends PlayException(title, description, cause) {

class SpringException( description: Option[String] = Option("The Spring application context is not started."),
                      cause: Option[Throwable] = None) extends UnexpectedException(description, cause) {
}
