#!/bin/bash
set -eu

yum -y install httpd
yum -y install mod_ssl

cp httpd/*.conf /etc/httpd/conf.d/

service httpd restart

# SELinux estää oletuksena mod_proxy:n käytön
(selinuxenabled && setsebool -P httpd_can_network_connect=1) || true

