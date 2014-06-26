import sbt._
import Keys._

object ApplicationBuild extends Build
{
	import play.Play.autoImport._
	import PlayKeys._
  
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

  val appName = "play-spring-module"
  val appVersion = "1.1-SNAPSHOT"

  val appDependencies = Seq(
          "org.springframework"    %    "spring-context"    %    "4.0.3.RELEASE",
          "org.springframework"    %    "spring-core"       %    "4.0.3.RELEASE",
          "org.springframework"    %    "spring-beans"      %    "4.0.3.RELEASE",
		      "cglib"    %    "cglib-nodep"      %    "3.1"
  )

  val main = Project(appName,  file(".")).enablePlugins(play.PlayJava).settings(
		version := appVersion,
		organization := "play"
  )

}
