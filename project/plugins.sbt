scalaVersion := "2.12.20"

val utilsVersion = "1.6.55"

val root = project
  .in(file("."))
  .settings(
    libraryDependencies ++= Seq("generic", "parser").map { m =>
      "io.circe" %% s"circe-$m" % "0.14.13"
    } ++ Seq(
      "com.malliina" %% "primitives" % "3.7.10",
      "commons-codec" % "commons-codec" % "1.18.0"
    ),
    Seq(
      "com.malliina" % "sbt-utils-maven" % utilsVersion,
      "com.malliina" % "sbt-nodejs" % utilsVersion,
      "com.malliina" % "sbt-revolver-rollup" % utilsVersion,
      "com.malliina" % "live-reload" % "0.6.0",
      "org.scala-js" % "sbt-scalajs" % "1.19.0",
      "org.scalameta" % "sbt-scalafmt" % "2.5.4",
      "com.eed3si9n" % "sbt-buildinfo" % "0.13.1",
      "org.scalameta" % "sbt-mdoc" % "2.7.1"
    ) map addSbtPlugin
  )
