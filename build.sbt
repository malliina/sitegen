import com.malliina.sbtutils.SbtUtils

inThisBuild(
  Seq(
    organization := "com.malliina",
    version := "1.0.1",
    scalaVersion := "3.2.2"
  )
)

val scalatagsVersion = GeneratorClientPlugin.scalatagsVersion

val frontend = project
  .in(file("frontend"))
  .enablePlugins(GeneratorClientPlugin, NodeJsPlugin, RollupPlugin)

val generator = project
  .in(file("generator"))
  .enablePlugins(GeneratorPlugin, NetlifyPlugin)
  .settings(
    clientProject := frontend,
    libraryDependencies ++= SbtUtils.loggingDeps ++ Seq(
      "com.malliina" %% "primitives" % "3.4.0",
      "com.lihaoyi" %% "scalatags" % scalatagsVersion
    )
  )

val site = project
  .in(file("."))
  .aggregate(frontend, generator)

Global / onChangedBuildSource := ReloadOnSourceChanges
