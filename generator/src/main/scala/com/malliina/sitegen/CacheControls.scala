package com.malliina.sitegen

import com.malliina.values.{StringCompanion, WrappedString}

case class CacheControl(value: String) extends WrappedString

object CacheControl extends StringCompanion[CacheControl]:
  val headerName = "Cache-Control"

object CacheControls extends CacheControls

trait CacheControls:
  val defaultCacheControl = CacheControl("public, max-age=0")
  val eternalCache = CacheControl("public, max-age=31536000")
