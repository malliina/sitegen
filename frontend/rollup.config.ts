import resolve from "@rollup/plugin-node-resolve"
import commonjs from "@rollup/plugin-commonjs"
import terser from "@rollup/plugin-terser"
import url from "@rollup/plugin-url"
import {scalajs, production, outputDir} from "./target/scalajs.rollup.config.js"
import path from "path"
import extractcss from "./rollup-extract-css"
import type {RollupOptions} from "rollup"

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
    useHash: production,
    hashOptions: {append: true}
  }
]

const entryNames = production ? "[name].[hash].js" : "[name].js"

const css = () => extractcss({
  outDir: outputDir,
  minimize: production,
  urlOptions: urlOptions
})

const config: RollupOptions[] = [
  {
    input: scalajs.input,
    plugins: [
      css(),
      resolve(), // tells Rollup how to find date-fns in node_modules
      commonjs(), // converts date-fns to ES modules
      production && terser() // minify, but only in production
    ],
    output: {
      dir: outputDir,
      format: "iife",
      name: "version",
      entryFileNames: entryNames
    },
    context: "window"
  },
  {
    input: {
      fonts: path.resolve(cssDir, "fonts.js"),
      styles: path.resolve(cssDir, "app.js")
    },
    plugins: [
      url({
        limit: 0,
        fileName: production ? "[dirname][name].[hash][extname]" : "[dirname][name][extname]"
      }),
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
