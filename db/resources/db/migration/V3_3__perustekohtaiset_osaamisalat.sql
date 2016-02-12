create table tutkinnonosa_ja_osaamisala_ja_peruste (
  osa varchar(6) not null references tutkinnonosa(osatunnus),
  osaamisala varchar(10) not null references osaamisala(osaamisalatunnus),
  peruste int not null references peruste(peruste_id),
  jarjestys int not null,
  tyyppi varchar(20) not null,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamp NOT NULL,
  luotuaika timestamp NOT NULL,
  PRIMARY KEY (osa, osaamisala, peruste)
);

create trigger tutkinnonosa_ja_osaamisala_ja_peruste_update before update on tutkinnonosa_ja_osaamisala_ja_peruste for each row execute procedure update_stamp() ;
create trigger tutkinnonosa_ja_osaamisala_ja_perustel_insert before insert on tutkinnonosa_ja_osaamisala_ja_peruste for each row execute procedure update_created() ;
create trigger tutkinnonosa_ja_osaamisala_ja_perustem_insert before insert on tutkinnonosa_ja_osaamisala_ja_peruste for each row execute procedure update_stamp() ;
create trigger tutkinnonosa_ja_osaamisala_ja_peruste_mu_update before update on tutkinnonosa_ja_osaamisala_ja_peruste for each row execute procedure update_modifier() ;
create trigger tutkinnonosa_ja_osaamisala_ja_peruste_cu_insert before insert on tutkinnonosa_ja_osaamisala_ja_peruste for each row execute procedure update_creator() ;
create trigger tutkinnonosa_ja_osaamisala_ja_peruste_mu_insert before insert on tutkinnonosa_ja_osaamisala_ja_peruste for each row execute procedure update_modifier() ;


insert into tutkinnonosa_ja_osaamisala_ja_peruste (osa, osaamisala, peruste, jarjestys, tyyppi)
select tjo.osa, tjo.osaamisala, ojp.peruste, tjo.jarjestys, tjo.tyyppi
from tutkinnonosa_ja_osaamisala tjo
inner join osaamisala_ja_peruste ojp on tjo.osaamisala = ojp.osaamisala;

drop table tutkinnonosa_ja_osaamisala;
insert into eperusteet_log (paivitetty) values ('1970-01-01'::timestamp)