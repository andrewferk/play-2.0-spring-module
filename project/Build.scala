import sbt._
import Keys._
import play.Project._

object Build extends Build
{

  // publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath+"/.m2/repository")))

  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

  val appName = "play-spring-module"
  val appVersion = "1.1-SNAPSHOT"

  val appDependencies = Seq(
          "org.springframework"    %    "spring-context"    %    "3.0.7.RELEASE",
          "org.springframework"    %    "spring-core"       %    "3.0.7.RELEASE",
          "org.springframework"    %    "spring-beans"      %    "3.0.7.RELEASE"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(organization := "play")

}
