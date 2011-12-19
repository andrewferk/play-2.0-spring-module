import _root_.sbt._
import _root_.sbt.Build
import _root_.sbt.Keys
import _root_.sbt.PlayProject
import _root_.sbt.PlayProject._
import _root_.sbt.Project._
import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build
{
  val appName = "play-2.0-spring-module"
  val appVersion = "1.0"

  val appDependencies = Seq(
    "play" %% "play" % "2.0-RC1-SNAPSHOT",
    "org.springframework" % "spring-context-support" % "3.0.6.RELEASE",
    "org.springframework" % "spring-core" % "3.0.6.RELEASE",
    "org.springframework" % "spring-asm" % "3.0.6.RELEASE",
    "org.slf4j" % "jcl-over-slf4j" % "1.6.1" ,
    "org.slf4j" % "log4j-over-slf4j" % "1.6.1" ,
    "org.slf4j" % "slf4j-api" % "1.6.1",
    "ch.qos.logback" % "logback-core" % "0.9.28",
    "ch.qos.logback" % "logback-classic" % "0.9.28"
  )

  /*
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "Coda Repository" at "http://repo.codahale.com",
      resolvers += "repo.novus rels" at "http://repo.novus.com/releases/",
      resolvers +=   "repo.novus snaps" at "http://repo.novus.com/snapshots/",
      resolvers +=   "JBoss Repository" at "https://repository.jboss.org/nexus/content/groups/public-jboss",
      resolvers +=   "Codehaus Repository" at "https://oss.sonatype.org/service/local/staging/deploy/maven2",
      resolvers +=   "glassfish Repository" at "http://download.java.net/maven/glassfish",
      resolvers +=   "apache repository" at "https://repository.apache.org/content/repositories/releases",
      resolvers +=   "scala tools repository" at "http://www.scala-tools.org/repo-releases",
      resolvers +=   "java.net repository" at "http://download.java.net/maven/2"
      */

  val main = Project(appName, base = file(".")).settings(

    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Coda Repository" at "http://repo.codahale.com",
    resolvers += "repo.novus rels" at "http://repo.novus.com/releases/",
    resolvers += "repo.novus snaps" at "http://repo.novus.com/snapshots/",
    resolvers += "JBoss Repository" at "https://repository.jboss.org/nexus/content/groups/public-jboss",
    resolvers += "Codehaus Repository" at "https://oss.sonatype.org/service/local/staging/deploy/maven2",
    resolvers += "glassfish Repository" at "http://download.java.net/maven/glassfish",
    resolvers += "apache repository" at "https://repository.apache.org/content/repositories/releases",
    resolvers += "scala tools repository" at "http://www.scala-tools.org/repo-releases",
    resolvers += "java.net repository" at "http://download.java.net/maven/2",

    version := appVersion,

    libraryDependencies ++= appDependencies)


}
