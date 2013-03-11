resolvers ++= Seq(
  DefaultMavenRepository,
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Other Repository" at "http://repo.typesafe.com/typesafe/repo/",
  "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
)

// Type "sbt gen-idea" to create an IntelliJ IDEA project.

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

// Type "sbt eclipse" to create an eclipse project

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse" % "2.1.0-RC1")

addSbtPlugin("play" % "sbt-plugin" % "2.1.0")

// Comment to get more information during initialization
logLevel := Level.Warn
