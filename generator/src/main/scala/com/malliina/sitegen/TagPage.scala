package com.malliina.sitegen

import com.malliina.build.{AppLogger, FileIO}
import com.malliina.sitegen.TagPage.DocTypeTag
import scalatags.Text

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, StandardOpenOption}

case class TagPage(tags: Text.TypedTag[String]):
  override def toString = tags.toString()
  def render = toString
  def renderDoc = DocTypeTag + render
  def write(to: Path) = FileIO.writeIfChanged(renderDoc, to)

object TagPage:
  private val DocTypeTag = "<!DOCTYPE html>"
