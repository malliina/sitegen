scalaVersion := "2.12.16"

libraryDependencies ++= Seq(
  "com.malliina" %% "primitives" % "3.2.0"
)

Seq(
  "com.malliina" % "sbt-utils-maven" % "1.2.15",
  "com.malliina" % "live-reload" % "0.3.1",
  "org.scala-js" % "sbt-scalajs" % "1.10.1",
  "ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.0",
  "org.scalameta" % "sbt-scalafmt" % "2.4.6",
  "com.eed3si9n" % "sbt-buildinfo" % "0.11.0"
) map addSbtPlugin
