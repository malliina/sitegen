scalaVersion := "2.12.20"

val utilsVersion = "1.6.41"

val root = project
  .in(file("."))
  .settings(
    libraryDependencies ++= Seq("generic", "parser").map { m =>
      "io.circe" %% s"circe-$m" % "0.14.10"
    } ++ Seq(
      "com.malliina" %% "primitives" % "3.7.4",
      "commons-codec" % "commons-codec" % "1.17.2"
    ),
    Seq(
      "com.malliina" % "sbt-utils-maven" % utilsVersion,
      "com.malliina" % "sbt-nodejs" % utilsVersion,
      "com.malliina" % "sbt-revolver-rollup" % utilsVersion,
      "com.malliina" % "live-reload" % "0.6.0",
      "org.scala-js" % "sbt-scalajs" % "1.17.0",
      "org.scalameta" % "sbt-scalafmt" % "2.5.2",
      "com.eed3si9n" % "sbt-buildinfo" % "0.13.1",
      "org.scalameta" % "sbt-mdoc" % "2.6.2"
    ) map addSbtPlugin
  )
