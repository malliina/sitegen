import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport.toPlatformDepsGroupID
import sbt.Keys._
import sbt._

object GeneratorClientPlugin extends AutoPlugin {
  val scalatagsVersion = "0.11.1"
//  override def requires = ScalaJSBundlerPlugin

  override def projectSettings: Seq[Setting[?]] = Seq(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.2.0",
      "com.lihaoyi" %%% "scalatags" % scalatagsVersion
    )
    // Enables hot-reload of CSS
//    watchSources ++= (Compile / resourceDirectories).value.map { dir =>
//      WatchSource(dir / "css", "*.less", HiddenFileFilter)
//    },
//    watchSources += WatchSource(baseDirectory.value / "src", "*.scala", HiddenFileFilter)
  )
}
