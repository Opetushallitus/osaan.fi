alter table osaamisala
  drop constraint osaamisala_pkey,
  drop column versio,
  drop column koodistoversio,
  drop column tutkinto,
  drop column voimassa_alkupvm,
  drop column voimassa_loppupvm,
  add primary key (osaamisalatunnus);

create table tutkinnonosa_ja_osaamisala (
  osa varchar(6) not null references tutkinnonosa(osatunnus),
  osaamisala varchar(5) not null references osaamisala(osaamisalatunnus),
  jarjestys int not null,
  tyyppi varchar(20) not null,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamp NOT NULL,
  luotuaika timestamp NOT NULL,
  PRIMARY KEY (osa, osaamisala)
);

create table osaamisala_ja_peruste (
  osaamisala varchar(5) not null references osaamisala(osaamisalatunnus),
  peruste integer not null references peruste(peruste_id),
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamp NOT NULL,
  luotuaika timestamp NOT NULL,
  PRIMARY KEY (osaamisala, peruste)
);

create trigger tutkinnonosa_ja_osaamisala_update before update on tutkinnonosa_ja_osaamisala for each row execute procedure update_stamp() ;
create trigger tutkinnonosa_ja_osaamisalal_insert before insert on tutkinnonosa_ja_osaamisala for each row execute procedure update_created() ;
create trigger tutkinnonosa_ja_osaamisalam_insert before insert on tutkinnonosa_ja_osaamisala for each row execute procedure update_stamp() ;
create trigger tutkinnonosa_ja_osaamisala_mu_update before update on tutkinnonosa_ja_osaamisala for each row execute procedure update_modifier() ;
create trigger tutkinnonosa_ja_osaamisala_cu_insert before insert on tutkinnonosa_ja_osaamisala for each row execute procedure update_creator() ;
create trigger tutkinnonosa_ja_osaamisala_mu_insert before insert on tutkinnonosa_ja_osaamisala for each row execute procedure update_modifier() ;

create trigger osaamisala_ja_peruste_update before update on osaamisala_ja_peruste for each row execute procedure update_stamp() ;
create trigger osaamisala_ja_perustel_insert before insert on osaamisala_ja_peruste for each row execute procedure update_created() ;
create trigger osaamisala_ja_perustem_insert before insert on osaamisala_ja_peruste for each row execute procedure update_stamp() ;
create trigger osaamisala_ja_peruste_mu_update before update on osaamisala_ja_peruste for each row execute procedure update_modifier() ;
create trigger osaamisala_ja_peruste_cu_insert before insert on osaamisala_ja_peruste for each row execute procedure update_creator() ;
create trigger osaamisala_ja_peruste_mu_insert before insert on osaamisala_ja_peruste for each row execute procedure update_modifier() ;
