import resolve from "@rollup/plugin-node-resolve"
import commonjs from "@rollup/plugin-commonjs"
import terser from "@rollup/plugin-terser"
import {scalajs, production, outputDir} from "./target/scalajs.rollup.config.js"
import path from "path"
import extractcss from "./rollup-extract-css"
import type {RollupOptions} from "rollup"
import {defaultSourcemapFix, sourcemaps} from "./rollup-sourcemaps"

const resourcesDir = "src/main/resources"
const cssDir = path.resolve(resourcesDir, "css")
const urlOptions = [
  {
    filter: "**/*.woff2",
    url: "inline"
  },
  // maxSize is kilobytes
  {
    filter: "**/*.png",
    url: "inline",
    maxSize: 48,
    fallback: "copy",
    assetsPath: "assets", // this must be defined but can be whatever since it "cancels out" the "../" in the source files
    useHash: true,
    hashOptions: {
      append: true
    }
  }
]

const entryNames = "[name].js"

const css = () => extractcss({
  outDir: outputDir,
  minimize: production,
  urlOptions: urlOptions
})

const config: RollupOptions[] = [
  {
    input: scalajs.input,
    plugins: [
      sourcemaps({}),
      css(),
      resolve(),
      commonjs(),
      production && terser()
    ],
    output: {
      dir: outputDir,
      format: "iife",
      name: "version",
      entryFileNames: entryNames,
      sourcemap: true,
      sourcemapPathTransform: (relativeSourcePath, sourcemapPath) =>
        defaultSourcemapFix(relativeSourcePath)
    },
    context: "window"
  },
  {
    input: {
      fonts: path.resolve(cssDir, "fonts.js"),
      styles: path.resolve(cssDir, "app.js")
    },
    plugins: [
      css(),
      production && terser()
    ],
    output: {
      dir: outputDir,
      entryFileNames: entryNames
    }
  }
]

export default config
