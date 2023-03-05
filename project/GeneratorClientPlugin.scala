import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport.toPlatformDepsGroupID
import sbt.Keys._
import sbt._

object GeneratorClientPlugin extends AutoPlugin {
  val scalatagsVersion = "0.12.0"

  override def projectSettings: Seq[Setting[?]] = Seq(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.4.0",
      "com.lihaoyi" %%% "scalatags" % scalatagsVersion
    )
  )
}
