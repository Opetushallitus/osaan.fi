#!/bin/bash

# Kääntää ja paketoi osaan.fi tietokannan version.
#
# Käyttö:
#
#     ./build.sh
#

set -eu

repo_path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

set -x

cd $repo_path
lein do clean, uberjar
