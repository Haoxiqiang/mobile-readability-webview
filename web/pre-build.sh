#!/usr/bin/env sh

npm install -g node-sass
npm install -g rollup

cd highlight.js
yarn build-cdn
cd ..

cd heti
yarn build
cd ..

