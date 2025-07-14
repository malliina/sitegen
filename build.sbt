import com.malliina.rollup.Git
import scala.sys.process.Process

inThisBuild(
  Seq(
    organization := "com.malliina",
    version := "1.0.1",
    scalaVersion := "3.7.0"
  )
)

val versions = new {
  val commonBuild = "1.6.55"
  val commonsCodec = "1.18.0"
  val logback = "1.5.18"
  val primitives = "3.7.10"
  val scalaJsDom = "2.8.0"
  val scalatags = "0.13.1"
}

val updateDocs = taskKey[Unit]("Updates README.md")

val frontend = project
  .in(file("frontend"))
  .enablePlugins(NodeJsPlugin, EsbuildPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % versions.scalaJsDom,
      "com.lihaoyi" %%% "scalatags" % versions.scalatags
    )
  )

val generator = project
  .in(file("generator"))
  .enablePlugins(GeneratorPlugin, NetlifyPlugin)
  .settings(
    scalajsProject := frontend,
    copyFolders += ((Compile / resourceDirectory).value / "public").toPath,
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "com.malliina" %% "primitives" % versions.primitives,
      "com.malliina" %% "common-build" % versions.commonBuild,
      "com.lihaoyi" %% "scalatags" % versions.scalatags,
      "commons-codec" % "commons-codec" % versions.commonsCodec
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
