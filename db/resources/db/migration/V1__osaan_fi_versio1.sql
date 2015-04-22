set session osaan.kayttaja='JARJESTELMA';

create table kayttaja(
    oid varchar(80) NOT NULL primary key,
    etunimi varchar(100) not null,
    sukunimi varchar(100) not null,
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamptz NOT NULL,
    luotuaika timestamptz NOT NULL,
    voimassa boolean not null default(true)
);
insert into kayttaja(oid, etunimi, sukunimi, voimassa, muutettuaika, luotuaika, luotu_kayttaja, muutettu_kayttaja)
values ('JARJESTELMA', 'Järjestelmä', '', true, current_timestamp, current_timestamp, 'JARJESTELMA', 'JARJESTELMA');

insert into kayttaja(oid, etunimi, sukunimi, voimassa, muutettuaika, luotuaika, luotu_kayttaja, muutettu_kayttaja)
values ('INTEGRAATIO', 'Integraatio', '', true, current_timestamp, current_timestamp, 'JARJESTELMA', 'JARJESTELMA');


create table koulutusala (
    koulutusala_tkkoodi varchar(3) not null primary key,
    selite_fi text not null,
    selite_sv text,
    selite_en text,
    voimassa_alkupvm date NOT NULL,
    voimassa_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamptz NOT NULL,
    luotuaika timestamptz NOT NULL
);


create table opintoala (
    opintoala_tkkoodi varchar(3) not null primary key,
    koulutusala_tkkoodi varchar(3) not null references koulutusala(koulutusala_tkkoodi),
    selite_fi text not null,
    selite_sv text,
    selite_en text,
    voimassa_alkupvm date NOT NULL,
    voimassa_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamptz NOT NULL,
    luotuaika timestamptz NOT NULL
);


CREATE OR REPLACE function update_stamp() returns trigger as $$ begin new.muutettuaika := now(); return new; end; $$ language plpgsql;
CREATE OR REPLACE function update_created() returns trigger as $$ begin new.luotuaika := now(); return new; end; $$ language plpgsql;
CREATE OR REPLACE function update_creator() returns trigger as $$ begin new.luotu_kayttaja := current_setting('osaan.kayttaja'); return new; end; $$ language plpgsql;
CREATE OR REPLACE function update_modifier() returns trigger as $$ begin new.muutettu_kayttaja := current_setting('osaan.kayttaja'); return new; end; $$ language plpgsql;

create trigger opintoala_update before update on opintoala for each row execute procedure update_stamp() ;
create trigger opintoalal_insert before insert on opintoala for each row execute procedure update_created() ;
create trigger opintoalam_insert before insert on opintoala for each row execute procedure update_stamp() ;
create trigger opintoala_mu_update before update on opintoala for each row execute procedure update_modifier() ;
create trigger opintoala_cu_insert before insert on opintoala for each row execute procedure update_creator() ;
create trigger opintoala_mu_insert before insert on opintoala for each row execute procedure update_modifier() ;

create trigger koulutusala_update before update on koulutusala for each row execute procedure update_stamp() ;
create trigger koulutusalal_insert before insert on koulutusala for each row execute procedure update_created() ;
create trigger koulutusalam_insert before insert on koulutusala for each row execute procedure update_stamp() ;
create trigger koulutusala_mu_update before update on koulutusala for each row execute procedure update_modifier() ;
create trigger koulutusala_cu_insert before insert on koulutusala for each row execute procedure update_creator() ;
create trigger koulutusala_mu_insert before insert on koulutusala for each row execute procedure update_modifier() ;


create table ohje(
  ohjetunniste varchar(80) NOT NULL primary key,
  teksti_fi text,
  teksti_sv text,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL
);

create trigger ohje_update before update on ohje for each row execute procedure update_stamp();
create trigger ohjel_insert before insert on ohje for each row execute procedure update_created();
create trigger ohjem_insert before insert on ohje for each row execute procedure update_stamp();
create trigger ohje_mu_update before update on ohje for each row execute procedure update_modifier();
create trigger ohje_mu_insert before insert on ohje for each row execute procedure update_modifier();
create trigger ohje_cu_insert before insert on ohje for each row execute procedure update_creator();


COMMENT ON TABLE koulutusala      IS 'Tilastokeskuksen luokittelun mukainen koulutusala';
COMMENT ON TABLE ohje  IS 'Käyttöohjeet, jotka näkyvät käyttöliittymässä';
COMMENT ON TABLE opintoala  IS 'Tilastokeskuksen luokittelun mukainen opintoala.';

-- tutkintorakenne ja perusteet

create table tutkintotaso(
    nimi varchar(25) not null primary key,
    kuvaus varchar(200),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamptz NOT NULL,
    luotuaika timestamptz NOT NULL
);

create table tutkintotyyppi (
    tyyppi varchar(2) not null primary key,
    selite_fi text not null,
    selite_sv text not null,
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamp NOT NULL,
    luotuaika timestamp NOT NULL
);


create table tutkinto (
    tutkintotunnus varchar(6) NOT NULL primary key,
    opintoala varchar(3) not null references opintoala(opintoala_tkkoodi),
    nimi_fi text not null,
    nimi_sv text,
    voimassa_alkupvm date NOT NULL,
    voimassa_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    siirtymaajan_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    tyyppi varchar(2) references tutkintotyyppi(tyyppi),
--    voimassaoleva_peruste varchar(20) references peruste(diaarinumero),
    tutkintotaso varchar(25) references tutkintotaso(nimi),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamptz NOT NULL,
    luotuaika timestamptz NOT NULL
);

create table peruste (
    diaarinumero varchar(20) not null primary key,
    alkupvm date not null,
    siirtymaajan_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    tutkinto varchar(6) NOT NULL references tutkinto(tutkintotunnus),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamp NOT NULL,
    luotuaika timestamp NOT NULL
);

create table tutkinnonosa (
   osatunnus varchar(6) not null PRIMARY KEY,
   tutkinto varchar(6) not null references tutkinto(tutkintotunnus),
   
   nimi text not null,
   muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   muutettuaika timestamptz NOT NULL,
   luotuaika timestamptz NOT NULL
);

create table tutkinnonosa_ja_peruste (
  osa varchar(6) not null references tutkinnonosa(osatunnus),
  peruste varchar(20) not null references peruste(diaarinumero),
  pakollinen boolean not null,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamp NOT NULL,
  luotuaika timestamp NOT NULL,
  PRIMARY KEY (osa, peruste)
);

create trigger tutkinnonosa_update before update on tutkinnonosa for each row execute procedure update_stamp() ;
create trigger tutkinnonosal_insert before insert on tutkinnonosa for each row execute procedure update_created() ;
create trigger tutkinnonosam_insert before insert on tutkinnonosa for each row execute procedure update_stamp() ;
create trigger tutkinnonosa_mu_update before update on tutkinnonosa for each row execute procedure update_modifier() ;
create trigger tutkinnonosa_cu_insert before insert on tutkinnonosa for each row execute procedure update_creator() ;
create trigger tutkinnonosa_mu_insert before insert on tutkinnonosa for each row execute procedure update_modifier() ;

create trigger tutkintotyyppi_update before update on tutkintotyyppi for each row execute procedure update_stamp() ;
create trigger tutkintotyyppil_insert before insert on tutkintotyyppi for each row execute procedure update_created() ;
create trigger tutkintotyyppim_insert before insert on tutkintotyyppi for each row execute procedure update_stamp() ;
create trigger tutkintotyyppi_mu_update before update on tutkintotyyppi for each row execute procedure update_modifier() ;
create trigger tutkintotyyppi_cu_insert before insert on tutkintotyyppi for each row execute procedure update_creator() ;
create trigger tutkintotyyppi_mu_insert before insert on tutkintotyyppi for each row execute procedure update_modifier() ;

create trigger tutkinto_update before update on tutkinto for each row execute procedure update_stamp() ;
create trigger tutkintol_insert before insert on tutkinto for each row execute procedure update_created() ;
create trigger tutkintom_insert before insert on tutkinto for each row execute procedure update_stamp() ;
create trigger tutkinto_mu_update before update on tutkinto for each row execute procedure update_modifier() ;
create trigger tutkinto_cu_insert before insert on tutkinto for each row execute procedure update_creator() ;
create trigger tutkinto_mu_insert before insert on tutkinto for each row execute procedure update_modifier() ;

create trigger tutkintotaso_update before update on tutkintotaso for each row execute procedure update_stamp() ;
create trigger tutkintotasol_insert before insert on tutkintotaso for each row execute procedure update_created() ;
create trigger tutkintotasom_insert before insert on tutkintotaso for each row execute procedure update_stamp() ;
create trigger tutkintotaso_mu_update before update on tutkintotaso for each row execute procedure update_modifier() ;
create trigger tutkintotaso_cu_insert before insert on tutkintotaso for each row execute procedure update_creator() ;
create trigger tutkintotaso_mu_insert before insert on tutkintotaso for each row execute procedure update_modifier() ;

create trigger peruste_update before update on peruste for each row execute procedure update_stamp() ;
create trigger perustel_insert before insert on peruste for each row execute procedure update_created() ;
create trigger perustem_insert before insert on peruste for each row execute procedure update_stamp() ;
create trigger peruste_mu_update before update on peruste for each row execute procedure update_modifier() ;
create trigger peruste_cu_insert before insert on peruste for each row execute procedure update_creator() ;
create trigger peruste_mu_insert before insert on peruste for each row execute procedure update_modifier() ;

create trigger tutkinnonosa_ja_peruste_update before update on tutkinnonosa_ja_peruste for each row execute procedure update_stamp() ;
create trigger tutkinnonosa_ja_perustel_insert before insert on tutkinnonosa_ja_peruste for each row execute procedure update_created() ;
create trigger tutkinnonosa_ja_perustem_insert before insert on tutkinnonosa_ja_peruste for each row execute procedure update_stamp() ;
create trigger tutkinnonosa_ja_peruste_mu_update before update on tutkinnonosa_ja_peruste for each row execute procedure update_modifier() ;
create trigger tutkinnonosa_ja_peruste_cu_insert before insert on tutkinnonosa_ja_peruste for each row execute procedure update_creator() ;
create trigger tutkinnonosa_ja_peruste_mu_insert before insert on tutkinnonosa_ja_peruste for each row execute procedure update_modifier() ;

