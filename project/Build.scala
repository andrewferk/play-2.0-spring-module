import sbt._
import Keys._
import PlayProject._

object Build extends sbt.Build
{

  // publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath+"/.m2/repository")))

  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

  val appName = "play-2.0-spring-module"
  val appVersion = "1.1-SNAPSHOT"

  val appDependencies = Seq(
          "org.springframework"    %    "spring-context"    %    "3.0.7.RELEASE",
          "org.springframework"    %    "spring-core"       %    "3.0.7.RELEASE",
          "org.springframework"    %    "spring-beans"      %    "3.0.7.RELEASE"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(organization := "play")

}
