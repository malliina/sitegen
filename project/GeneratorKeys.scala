import sbt.{File, settingKey, taskKey}

object GeneratorKeys {
  val build = taskKey[Unit]("Builds app")
  val isProd = settingKey[Boolean]("true if in prod mode, false otherwise")
  val siteDir = settingKey[File]("Site directory")
}
