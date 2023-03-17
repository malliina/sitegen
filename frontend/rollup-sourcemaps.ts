import type {ExistingRawSourceMap, Plugin} from "rollup"
import {createFilter} from "@rollup/pluginutils"
import * as fs from "fs";

export interface SourcemapOptions {
  include?: string
  exclude?: string
}

export default function sourcemaps(options: SourcemapOptions): Plugin {
  const filter = createFilter(options.include || "**/main.js", options.exclude)
  return {
    name: "rollup-plugin-sourcemaps",
    async load(id: string) {
      if (!filter(id)) return
      const mapFile = `${id}.map`
      try {
        await fs.promises.stat(mapFile)
      } catch (e) {
        return
      }
      const code = await fs.promises.readFile(id, "utf-8")
      const sourcemap = await fs.promises.readFile(mapFile, "utf-8")
      const existing: ExistingRawSourceMap = JSON.parse(sourcemap)
      return { code: code, map: existing }
    }
  }
}
