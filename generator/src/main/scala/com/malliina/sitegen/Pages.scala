package com.malliina.sitegen

import com.malliina.live.LiveReload
import com.malliina.sitegen.Pages.*
import scalatags.Text
import scalatags.Text.all.*
import scalatags.text.Builder

object Pages:
  val time = tag("time")
  val titleTag = tag("title")

  val datetime = attr("datetime")
  val property = attr("property")

  def attrType[T](stringify: T => String): AttrValue[T] = (t: Builder, a: Attr, v: T) =>
    t.setAttr(a.name, Builder.GenericAttrValueSource(stringify(v)))

class Pages(isProd: Boolean):
  private val globalDescription = "The best site."

  private val scripts =
    if isProd then scriptAt(FileAssets.frontend_js, defer)
    else
      modifier(
        scriptAt(FileAssets.frontend_js),
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
          content := "width=device-width, initial-scale=1.0"
        ),
        link(
          rel := "shortcut icon",
          `type` := "image/png",
          href := fileOrInline(FileAssets.img.jag_16x16_png)
        ),
        meta(name := "description", content := globalDescription),
        meta(name := "keywords", content := "Site"),
        meta(property := "og:title", content := titleText),
        meta(property := "og:description", content := globalDescription),
        styleAt(FileAssets.styles_css),
        styleAt(FileAssets.fonts_css)
      ),
      body(
        contents :+ scripts
      )
    )
  )

  private def styleAt(file: String): Text.TypedTag[String] =
    link(rel := "stylesheet", href := findAsset(file))
  private def scriptAt(file: String, modifiers: Modifier*): Text.TypedTag[String] =
    script(src := findAsset(file), modifiers)
  private def fileOrInline(file: String) =
    HashedAssets.dataUris.getOrElse(file, HashedAssets.assets.getOrElse(file, file))
  private def findAsset(file: String) = HashedAssets.assets.getOrElse(file, file)
