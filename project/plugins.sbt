scalaVersion := "2.12.17"

val utilsVersion = "1.5.0"

lazy val commonRef = RootProject(file("../common"))

lazy val root = project
  .in(file("."))
  .dependsOn(commonRef)
  .settings(
    libraryDependencies ++= Seq(
      "com.malliina" %% "primitives" % "3.4.0",
      "commons-codec" % "commons-codec" % "1.15"
    ),
    Seq(
      "com.malliina" % "sbt-utils-maven" % utilsVersion,
      "com.malliina" % "sbt-nodejs" % utilsVersion,
      "com.malliina" % "live-reload" % "0.5.0",
      "org.scala-js" % "sbt-scalajs" % "1.13.0",
      "org.scalameta" % "sbt-scalafmt" % "2.5.0",
      "com.eed3si9n" % "sbt-buildinfo" % "0.11.0"
    ) map addSbtPlugin
  )
