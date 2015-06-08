#!/bin/bash
set -eu

REPO_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"

set -x

cd $REPO_PATH/vagrant
vagrant ssh osaan-db -c 'cd /env && ./pgload.sh /dumps/osaan-dump.db dev-db.pgpass osaan osaan_adm 127.0.0.1 5432'

cd $REPO_PATH/db
eval lein run 'postgresql://osaan_adm:osaan-adm@127.0.0.1:4567/osaan' -u osaan_user

cd $REPO_PATH/ansible
chmod 600 yhteiset/dev_id_rsa
ssh-add yhteiset/dev_id_rsa
ansible-playbook -i osaan_vagrant/hosts yhteiset/konfiguroi_tietokanta.yml