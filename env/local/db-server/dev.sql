create user osaan_adm with password 'osaan-adm';
CREATE DATABASE osaan;
GRANT ALL PRIVILEGES ON DATABASE osaan to osaan_adm;

create user osaan_user with password 'osaan';
GRANT CONNECT ON DATABASE osaan TO osaan_user;

GRANT osaan_user TO osaan_adm;
