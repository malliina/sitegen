scalaVersion := "2.12.18"

val utilsVersion = "1.6.28"

val root = project
  .in(file("."))
  .settings(
    libraryDependencies ++= Seq("generic", "parser").map { m =>
      "io.circe" %% s"circe-$m" % "0.14.6"
    } ++ Seq(
      "com.malliina" %% "primitives" % "3.4.6",
      "commons-codec" % "commons-codec" % "1.16.0"
    ),
    Seq(
      "com.malliina" % "sbt-utils-maven" % utilsVersion,
      "com.malliina" % "sbt-nodejs" % utilsVersion,
      "com.malliina" % "sbt-revolver-rollup" % utilsVersion,
      "com.malliina" % "live-reload" % "0.5.0",
      "org.scala-js" % "sbt-scalajs" % "1.14.0",
      "org.scalameta" % "sbt-scalafmt" % "2.5.2",
      "com.eed3si9n" % "sbt-buildinfo" % "0.11.0",
      "org.scalameta" % "sbt-mdoc" % "2.4.0"
    ) map addSbtPlugin
  )
