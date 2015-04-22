#!/bin/bash
set -eu

repo_path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"

set -x

cd $repo_path/db
lein run 'postgresql://osaan_adm:osaan-adm@127.0.0.1:4567/osaan' -u osaan_user --clear $@

cd $repo_path/ansible
chmod 600 yhteiset/dev_id_rsa
ssh-add yhteiset/dev_id_rsa
ansible-playbook -i osaan_vagrant/hosts yhteiset/konfiguroi_tietokanta.yml
