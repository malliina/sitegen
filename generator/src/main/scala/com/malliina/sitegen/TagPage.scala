package com.malliina.sitegen

import com.malliina.sitegen.TagPage.DocTypeTag

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, StandardOpenOption}
import scalatags.Text

case class TagPage(tags: Text.TypedTag[String]):
  override def toString = tags.toString()
  def render = toString
  def renderDoc = DocTypeTag + render
  def write(to: Path) = FileIO.writeIfChanged(renderDoc, to)

object TagPage:
  val log = AppLogger(getClass)
  private val DocTypeTag = "<!DOCTYPE html>"
