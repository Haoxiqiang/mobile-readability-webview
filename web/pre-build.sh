#!/usr/bin/env sh

npm install -g pnpm
pnpm install -g node-sass
pnpm install -g commander
pnpm install -g rollup

cd highlight.js
pnpm install
pnpm build-cdn
cd ..

cd heti
npm install
npm build
cd ..

