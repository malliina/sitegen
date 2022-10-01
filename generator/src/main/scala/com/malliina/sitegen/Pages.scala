package com.malliina.sitegen

import com.malliina.http.FullUrl
import com.malliina.live.LiveReload
import com.malliina.sitegen.Pages.*
import scalatags.Text
import scalatags.Text.all.*
import scalatags.text.Builder

import java.nio.file.{Files, Path}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Pages:
  val time = tag("time")
  val titleTag = tag("title")

  val datetime = attr("datetime")
  val property = attr("property")

  def attrType[T](stringify: T => String): AttrValue[T] = (t: Builder, a: Attr, v: T) =>
    t.setAttr(a.name, Builder.GenericAttrValueSource(stringify(v)))

class Pages(isProd: Boolean, root: Path):
  val globalDescription = "Meny."

  val scripts =
    if isProd then scriptAt("frontend-opt.js", defer)
    else
      modifier(
        scriptAt("library.js"),
        scriptAt("loader.js"),
        scriptAt("app.js"),
        script(src := LiveReload.script)
      )

  def hello = index("Hello")(
    div(`class` := "content")(
      p("Hello, world!")
    )
  )

  def index(titleText: String)(contents: Modifier*): TagPage = TagPage(
    html(lang := "en")(
      head(
        titleTag(titleText),
        meta(charset := "UTF-8"),
        meta(
          name := "viewport",
          content := "width=device-width, initial-scale=1.0, maximum-scale=1.0"
        ),
        meta(name := "description", content := globalDescription),
        meta(name := "keywords", content := "Site"),
        meta(property := "og:title", content := titleText),
        meta(property := "og:description", content := globalDescription),
        styleAt("styles.css"),
        styleAt("fonts.css")
      ),
      body(
        contents :+ scripts
      )
    )
  )

  def styleAt(file: String): Text.TypedTag[String] =
    link(rel := "stylesheet", href := findAsset(file))

  def scriptAt(file: String, modifiers: Modifier*): Text.TypedTag[String] =
    script(src := findAsset(file), modifiers)

  def findAsset(file: String): String =
    val path = root.resolve(file)
    val dir = path.getParent
    val candidates = Files.list(dir).iterator().asScala.toList
    val lastSlash = file.lastIndexOf("/")
    val nameStart = if lastSlash == -1 then 0 else lastSlash + 1
    val name = file.substring(nameStart)
    val dotIdx = name.lastIndexOf(".")
    val noExt = name.substring(0, dotIdx)
    val ext = name.substring(dotIdx + 1)
    val result = candidates.filter { p =>
      val candidateName = p.getFileName.toString
      candidateName.startsWith(noExt) && candidateName.endsWith(ext)
    }.sortBy { p => Files.getLastModifiedTime(p) }.reverse.headOption
    val found = result.getOrElse(
      fail(s"Not found: '$file'. Found ${candidates.mkString(", ")}.")
    )
    root.relativize(found).toString.replace("\\", "/")

  def fail(message: String) = throw new Exception(message)
