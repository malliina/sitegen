import com.malliina.live.LiveReloadPlugin
import com.malliina.live.LiveReloadPlugin.autoImport.{liveReloadRoot, refreshBrowsers, reloader}
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.{fastLinkJS, fullLinkJS}
import sbt.Keys.{run, sourceGenerators, watchSources}
import sbt._
import sbtbuildinfo.BuildInfoPlugin
import sbtbuildinfo.BuildInfoPlugin.autoImport.{BuildInfoKey, buildInfoKeys}
import HashPlugin.autoImport.{hash, hashRoot}
import com.malliina.sitegen.Mode

object GeneratorPlugin extends AutoPlugin {
  override def requires = BuildInfoPlugin && LiveReloadPlugin && HashPlugin

  object autoImport {
    val mode = settingKey[Mode]("Build mode, dev or prod")

    val DevMode = Mode.Dev
    val ProdMode = Mode.Prod

    val clientProject = settingKey[Project]("Scala.js project")
  }
  import GeneratorKeys._
  import autoImport._

  override def projectSettings: Seq[Setting[?]] = Seq(
    isProd := ((Global / mode).value == Mode.prod),
    siteDir := Def.settingDyn { clientProject.value / siteDir }.value,
    hashRoot := siteDir.value,
    liveReloadRoot := siteDir.value.toPath,
    buildInfoKeys ++= Seq[BuildInfoKey](
      "siteDir" -> siteDir.value,
      "isProd" -> isProd.value
    ),
    refreshBrowsers := refreshBrowsers.triggeredBy(build).value,
    build := Def.taskDyn {
      val jsTask = if (isProd.value) fullLinkJS else fastLinkJS
      (Compile / run)
        .toTask(" ")
        .dependsOn(Def.task(if (isProd.value) () else reloader.value.start()))
        .dependsOn(hash)
        .dependsOn(clientProject.value / Compile / jsTask / build)
    }.value,
    watchSources := watchSources.value ++ Def.taskDyn(clientProject.value / watchSources).value,
    Compile / sourceGenerators += hash
  )

  override def globalSettings: Seq[Setting[?]] = Seq(
    mode := Mode.dev
  )
}
