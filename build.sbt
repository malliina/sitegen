import com.malliina.sbtutils.SbtUtils
import com.malliina.rollup.Git
import scala.sys.process.Process

inThisBuild(
  Seq(
    organization := "com.malliina",
    version := "1.0.1",
    scalaVersion := "3.4.2"
  )
)

val scalatagsVersion = "0.13.1"

val updateDocs = taskKey[Unit]("Updates README.md")

val frontend = project
  .in(file("frontend"))
  .enablePlugins(NodeJsPlugin, RollupPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.8.0",
      "com.lihaoyi" %%% "scalatags" % scalatagsVersion
    )
  )

val generator = project
  .in(file("generator"))
  .enablePlugins(GeneratorPlugin, NetlifyPlugin)
  .settings(
    scalajsProject := frontend,
    copyFolders += ((Compile / resourceDirectory).value / "public").toPath,
    libraryDependencies ++= SbtUtils.loggingDeps ++ Seq(
      "ch.qos.logback" % "logback-classic" % "1.5.7",
      "com.malliina" %% "primitives" % "3.7.3",
      "com.malliina" %% "common-build" % "1.6.40",
      "com.lihaoyi" %% "scalatags" % scalatagsVersion,
      "commons-codec" % "commons-codec" % "1.17.1"
    ),
    hashPackage := "com.malliina.sitegen",
    buildInfoKeys += "gitHash" -> Git.gitHash
  )

val docs = project
  .in(file("mdoc"))
  .settings(
    publish / skip := true,
    mdocVariables := Map(
      "VERSION" -> version.value,
      "ASSETS_ROOT" -> (ThisBuild / baseDirectory).value.toPath
        .relativize((frontend / assetsRoot).value)
        .toString
    ),
    mdocOut := target.value / "docs",
    mdocExtraArguments += "--no-link-hygiene",
    updateDocs := {
      val log = streams.value.log
      val outFile = mdocOut.value / "README.md"
      val rootReadme = (ThisBuild / baseDirectory).value / "README.md"
      IO.copyFile(outFile, rootReadme)
      val addStatus = Process(s"git add $rootReadme").run(log).exitValue()
      if (addStatus != 0) {
        sys.error(s"Unexpected status code $addStatus for git commit.")
      }
    },
    updateDocs := updateDocs.dependsOn(mdoc.toTask("")).value
  )
  .enablePlugins(MdocPlugin)

val site = project
  .in(file("."))
  .aggregate(frontend, generator, docs)

Global / onChangedBuildSource := ReloadOnSourceChanges
