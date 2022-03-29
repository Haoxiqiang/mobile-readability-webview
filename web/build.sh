#!/usr/bin/env sh

# build styles
dist=dist/readerview
sass mozilla-reader.sass $dist/css/mozilla-reader.css --no-source-map
sass mozilla-reader-dark.sass $dist/css/mozilla-reader-dark.css --no-source-map
sass mozilla-reader-light.sass $dist/css/mozilla-reader-light.css --no-source-map
sass mozilla-reader-sepia.sass $dist/css/mozilla-reader-sepia.css --no-source-map

# build high-light
echo `pwd`
cd highlight.js/ && yarn install && yarn build-cdn && cd ..
cp highlight.js/build/es/highlight.js $dist/js/highlight.js
echo 'window.hljs = highlight;' >> $dist/js/highlight.js
cp highlight.js/build/styles/default.min.css $dist/css/highlight.min.css

# build heti
cp heti/_site/heti.css $dist/css/heti.css
cp heti/_site/heti-addon.js $dist/js/heti-addon.js

# build readability-0.4.2
cp readability/JSDOMParser.js $dist/readability-0.4.2/JSDOMParser.js
cp readability/Readability.js $dist/readability-0.4.2/Readability.js
cp readability/Readability-readerable.js $dist/readability-0.4.2/Readability-readerable.js

# build compat
cp html-compat.js $dist/js/html-compat.js