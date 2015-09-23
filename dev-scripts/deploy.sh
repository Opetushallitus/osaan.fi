#!/bin/bash
set -eu

repo_path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"

set -x

cd $repo_path/app
./build.sh

cd $repo_path/db
./build.sh

cd $repo_path/ansible
chmod 600 yhteiset/dev_id_rsa
ssh-add yhteiset/dev_id_rsa
ansible-playbook -v -i osaan_vagrant/hosts yhteiset/julkaise_paikallinen_versio.yml -e "sovellus_jar=\"$repo_path/app/target/osaan-standalone.jar\" migraatio_jar=\"$repo_path/db/target/osaan-db-standalone.jar\""
