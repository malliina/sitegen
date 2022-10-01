# sitegen

A static site generator and template. Deploys to [Netlify](https://www.netlify.com/).

The template uses:

- [Scalatags](https://com-lihaoyi.github.io/scalatags/) for HTML generation
- [Scala.js](https://www.scala-js.org/) for JS code
- [less](https://lesscss.org/) for styling

## Dependencies

- [Scala](https://docs.scala-lang.org/) 3.x
- [node.js](https://nodejs.org/en/) 16.x

## Development

    sbt ~build

Navigate to http://localhost:10101. The site will live reload on code changes.

## Deployment

1. Download the Netlify [CLI](https://docs.netlify.com/cli/get-started/).
1. Obtain and set environment variables `NETLIFY_AUTH_TOKEN` and `NETLIFY_SITE_ID`.
1. Run:

       sbt "set Global / mode := ProdMode" deploy
