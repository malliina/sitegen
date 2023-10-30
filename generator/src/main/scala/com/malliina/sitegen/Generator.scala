package com.malliina.sitegen

import buildinfo.BuildInfo
import com.malliina.build.{AppLogger, FileIO}
import io.circe.Json
import io.circe.syntax.EncoderOps

import java.nio.file.{Files, Path}

object Generator:
  val log = AppLogger(getClass)

  def main(args: Array[String]): Unit =
    generate(BuildInfo.isProd, BuildInfo.siteDir.toPath)

  def generate(isProd: Boolean, dist: Path): Unit =
    val mode = if isProd then "prod" else "dev"
    log.info(s"Generating $mode build to $dist...")
    Files.createDirectories(dist)
    val pages = Pages(isProd)
    val pageMap = Map(
      pages.hello -> "index.html"
    )
    pageMap.foreach: (page, file) =>
      val dest = dist.resolve(file)
      if FileIO.mismatch(page.renderDoc, dest) then page.write(dest)
    val health = dist.resolve("health")
    val healthJson = Json.obj("version" -> BuildInfo.gitHash.asJson)
    FileIO.writeIfChanged(healthJson.noSpaces, health)
    NetlifyClient.writeHeaders(dist)
