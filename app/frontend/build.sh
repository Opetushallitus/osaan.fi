#!/bin/bash

# Kääntää ja paketoi osaan.fi frontin.
#
# Käyttö:
#
#     ./build.sh [-t]
#
# Parametrit:
#     -t              Jos annettu, aja yksikkötestit.

set -eu

run_tests=no
while getopts 't' o; do
    case $o in
        t)
            run_tests=yes
            ;;
    esac
done

set -x

npm --version
npm install
rm -rf src/bower_components
bower install
grunt build
if [ "$run_tests" = yes ]; then
    grunt test_ff  --no-color
fi
