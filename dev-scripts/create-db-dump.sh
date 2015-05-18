#!/bin/bash
set -eu

repo_path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"

set -x

cd $repo_path/vagrant
vagrant ssh osaan-db -c 'cd /env && ./pgdump.sh /dumps/osaan-dump.db dev-db.pgpass osaan osaan_adm 127.0.0.1 5432'
