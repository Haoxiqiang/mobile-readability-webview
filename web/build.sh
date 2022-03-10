#!/usr/bin/env sh

sass mozilla-reader.sass ../webview/src/main/assets/readerview/mozilla/mozilla-reader.css --no-source-map
sass mozilla-reader-dark.sass ../webview/src/main/assets/readerview/mozilla/mozilla-reader-dark.css --no-source-map
sass mozilla-reader-light.sass ../webview/src/main/assets/readerview/mozilla/mozilla-reader-light.css --no-source-map
sass mozilla-reader-sepia.sass ../webview/src/main/assets/readerview/mozilla/mozilla-reader-sepia.css --no-source-map

cp highlight.js/build/es/highlight.js ../webview/src/main/assets/readerview/js/highlight.js
echo 'window.hljs = hljs;' >> ../webview/src/main/assets/readerview/js/highlight.js
cp highlight.js/build/styles/default.min.css ../webview/src/main/assets/readerview/css/highlight.min.css