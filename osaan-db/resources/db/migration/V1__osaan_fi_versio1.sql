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
