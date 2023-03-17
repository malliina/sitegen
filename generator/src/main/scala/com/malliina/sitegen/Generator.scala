package com.malliina.sitegen

import buildinfo.BuildInfo
import com.malliina.build.{AppLogger, FileIO}
import org.apache.commons.codec.digest.DigestUtils

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}

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
    pageMap.foreach { case (page, file) =>
      val dest = dist.resolve(file)
      if FileIO.mismatch(page.renderDoc, dest) then page.write(dest)
    }
    val health = dist.resolve("health")
    FileIO.writeIfChanged(s"""{"version": "${BuildInfo.gitHash}"}""", health)
    NetlifyClient.writeHeaders(dist)
