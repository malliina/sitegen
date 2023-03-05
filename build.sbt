import com.malliina.sbtutils.SbtUtils
import scala.sys.process.Process
import scala.util.Try

inThisBuild(
  Seq(
    organization := "com.malliina",
    version := "1.0.1",
    scalaVersion := "3.2.2"
  )
)

val scalatagsVersion = GeneratorClientPlugin.scalatagsVersion

val common = project.in(file("common"))

val frontend = project
  .in(file("frontend"))
  .enablePlugins(GeneratorClientPlugin, NodeJsPlugin, RollupPlugin)

val generator = project
  .in(file("generator"))
  .dependsOn(common)
  .enablePlugins(GeneratorPlugin, NetlifyPlugin)
  .settings(
    clientProject := frontend,
    copyFolders += (Compile / resourceDirectory).value / "public",
    libraryDependencies ++= SbtUtils.loggingDeps ++ Seq(
      "com.malliina" %% "primitives" % "3.4.0",
      "com.lihaoyi" %% "scalatags" % scalatagsVersion,
      "commons-codec" % "commons-codec" % "1.15"
    ),
    buildInfoKeys += "gitHash" -> gitHash
  )

val site = project
  .in(file("."))
  .aggregate(frontend, generator)

def gitHash: String =
  sys.env
    .get("GITHUB_SHA")
    .orElse(Try(Process("git rev-parse HEAD").lineStream.head).toOption)
    .getOrElse("unknown")

Global / onChangedBuildSource := ReloadOnSourceChanges
