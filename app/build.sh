#!/bin/bash

# Kääntää ja paketoi osaan.fi version.
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

repo_path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

set -x

cd "$repo_path/frontend"
npm run build "$@"

cd "$repo_path"
if [ "$run_tests" = yes ]; then
    lein do test, clean, uberjar
else
    lein do clean, uberjar
fi

