import com.malliina.sbtutils.SbtUtils
import com.malliina.rollup.UrlOption
import com.malliina.build.FileIO
import scala.sys.process.Process
import scala.util.Try
import java.nio.file.Path
import java.nio.file.Files

inThisBuild(
  Seq(
    organization := "com.malliina",
    version := "1.0.1",
    scalaVersion := "3.2.2"
  )
)

val scalatagsVersion = "0.12.0"

val updateDocs = taskKey[Unit]("Updates README.md")
val packageLock = taskKey[Path]("Package lock")

val docs = project
  .in(file("mdoc"))
  .settings(
    publish / skip := true,
    mdocVariables := Map("VERSION" -> version.value),
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

val frontend = project
  .in(file("frontend"))
  .enablePlugins(NodeJsPlugin, RollupPlugin, RollupPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.4.0",
      "com.lihaoyi" %%% "scalatags" % scalatagsVersion
    ),
    packageLock := {
      val lockFile = (Compile / resourceDirectory).value.toPath.resolve("package-lock.json")
      val dest = npmRoot.value / "package-lock.json"
      if (Files.exists(lockFile)) {
        FileIO.copyIfChanged(lockFile, dest)
      }
      dest
    },
    fullLinkJS / prepareRollup := (fullLinkJS / prepareRollup).dependsOn(packageLock).value
  )

val generator = project
  .in(file("generator"))
  .enablePlugins(GeneratorPlugin, NetlifyPlugin)
  .settings(
    scalajsProject := frontend,
    copyFolders += ((Compile / resourceDirectory).value / "public").toPath,
    libraryDependencies ++= SbtUtils.loggingDeps ++ Seq(
      "com.malliina" %% "primitives" % "3.4.0",
      "com.malliina" %% "common-build" % "1.6.7",
      "com.lihaoyi" %% "scalatags" % scalatagsVersion,
      "commons-codec" % "commons-codec" % "1.15"
    ),
    hashPackage := "com.malliina.sitegen",
    buildInfoKeys += "gitHash" -> gitHash
  )

val site = project
  .in(file("."))
  .aggregate(frontend, generator, docs)

def gitHash: String =
  sys.env
    .get("GITHUB_SHA")
    .orElse(Try(Process("git rev-parse HEAD").lineStream.head).toOption)
    .getOrElse("unknown")

Global / onChangedBuildSource := ReloadOnSourceChanges
