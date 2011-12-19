resolvers ++= Seq(
  DefaultMavenRepository,
  Resolver.url("Local Repository", url("file:///Users/wsargent/play-2.0-RC1-SNAPSHOT/repository")),
  Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Other Repository" at "http://repo.typesafe.com/typesafe/repo/",
  "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "0.11.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse" % "1.5.0")

addSbtPlugin("play" % "sbt-plugin" % "2.0-RC1-SNAPSHOT")

libraryDependencies += "org.springframework" % "spring-context-support" % "3.0.6.RELEASE"

libraryDependencies += "org.springframework" % "spring-core" % "3.0.6.RELEASE"

libraryDependencies += "org.springframework" % "spring-asm" % "3.0.6.RELEASE"
