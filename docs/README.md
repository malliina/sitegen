# sitegen

A static site generator and template. Deploys to [Netlify](https://www.netlify.com/).

The template uses:

- [Scalatags](https://com-lihaoyi.github.io/scalatags/) for HTML generation
- [Scala.js](https://www.scala-js.org/) for JS code
- [css nesting](https://www.w3.org/TR/css-nesting-1/) for CSS

## Dependencies

- [Scala](https://docs.scala-lang.org/) 3.x
- [node.js](https://nodejs.org/en/) 16.x
- [rollup.js](https://www.rollupjs.org/) 4.x

## Development

    sbt ~build

Navigate to http://localhost:10101. The site will reload on code changes.

The `build` task builds a static website to `@ASSETS_ROOT@`.

## Deployment

1. Download the Netlify [CLI](https://docs.netlify.com/cli/get-started/).
1. Obtain and set environment variables `NETLIFY_AUTH_TOKEN` and `NETLIFY_SITE_ID`.
1. Run:

       sbt "mode prod" deploy
