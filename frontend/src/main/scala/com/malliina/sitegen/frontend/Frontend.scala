package com.malliina.sitegen.frontend

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("./css/app", JSImport.Namespace)
object AppCss extends js.Object

object Frontend:
  private val appCss = AppCss

  def main(args: Array[String]): Unit =
    println("Hello, world!!!")
