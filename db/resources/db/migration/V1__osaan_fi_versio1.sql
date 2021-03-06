set session osaan.kayttaja='JARJESTELMA';

CREATE SEQUENCE ammattitaidonkuvaus_id_seq;
CREATE SEQUENCE arvioinninkohdealue_id_seq;
CREATE SEQUENCE peruste_id_seq;
CREATE SEQUENCE eperusteet_log_id_seq;

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
    koulutusalatunnus varchar(3) not null primary key,
    nimi_fi text not null,
    nimi_sv text,
    nimi_en text,
    voimassa_alkupvm date NOT NULL,
    voimassa_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamptz NOT NULL,
    luotuaika timestamptz NOT NULL
);


create table opintoala (
    opintoalatunnus varchar(3) not null primary key,
    koulutusala varchar(3) not null references koulutusala(koulutusalatunnus),
    nimi_fi text not null,
    nimi_sv text,
    nimi_en text,
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
    opintoala varchar(3) not null references opintoala(opintoalatunnus),
    nimi_fi text not null,
    nimi_sv text,
    voimassa_alkupvm date NOT NULL,
    voimassa_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    siirtymaajan_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    tyyppi varchar(2) references tutkintotyyppi(tyyppi),
    tutkintotaso varchar(25) references tutkintotaso(nimi),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamptz NOT NULL,
    luotuaika timestamptz NOT NULL
);

create table perustetyyppi (
    tunnus varchar(6) NOT NULL primary key,
    kuvaus varchar(200),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamp NOT NULL,
    luotuaika timestamp NOT NULL
);

create table peruste (
    peruste_id integer not null primary key default nextval('peruste_id_seq'), 
    diaarinumero varchar(20) not null,
    eperustetunnus integer not null,
    voimassa_alkupvm date not null,
    voimassa_loppupvm date not null DEFAULT to_date('21990101', 'YYYYMMDD'),
    siirtymaajan_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
    tyyppi varchar(6) NOT NULL references perustetyyppi(tunnus),
    tutkinto varchar(6) NOT NULL references tutkinto(tutkintotunnus),
    muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
    muutettuaika timestamp NOT NULL,
    luotuaika timestamp NOT NULL
);

create table tutkintonimike(
  nimiketunnus varchar(6) not null primary key,
  nimi_fi text not null,
  nimi_sv text,
  tutkinto varchar(6) not null references tutkinto(tutkintotunnus),
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL
);

create table tutkinnonosa (
   osatunnus varchar(6) not null PRIMARY KEY,
   nimi_fi text not null,
   nimi_sv text ,
   muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   muutettuaika timestamptz NOT NULL,
   luotuaika timestamptz NOT NULL
);

create table osaamisala(
   osaamisalatunnus	varchar(5) not null,
   versio integer not null,
   koodistoversio integer not null,
   tutkinto varchar(6) not null references tutkinto(tutkintotunnus),
   nimi_fi text not null,
   nimi_sv text,
   voimassa_alkupvm date NOT NULL,
   voimassa_loppupvm date NOT NULL DEFAULT to_date('21990101', 'YYYYMMDD'),
   muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   muutettuaika timestamp NOT NULL,
   luotuaika timestamp NOT NULL,
   PRIMARY KEY(osaamisalatunnus, versio, koodistoversio)
);

create table tutkinnonosa_ja_peruste (
  osa varchar(6) not null references tutkinnonosa(osatunnus),
  peruste integer not null references peruste(peruste_id),
  jarjestys int not null,
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

create trigger tutkintonimike_update before update on tutkintonimike for each row execute procedure update_stamp() ;
create trigger tutkintonimikel_insert before insert on tutkintonimike for each row execute procedure update_created() ;
create trigger tutkintonimikem_insert before insert on tutkintonimike for each row execute procedure update_stamp() ;
create trigger tutkintonimike_mu_update before update on tutkintonimike for each row execute procedure update_modifier() ;
create trigger tutkintonimike_cu_insert before insert on tutkintonimike for each row execute procedure update_creator() ;
create trigger tutkintonimike_mu_insert before insert on tutkintonimike for each row execute procedure update_modifier() ;

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


create trigger perustetyyppi_update before update on perustetyyppi for each row execute procedure update_stamp() ;
create trigger perustetyyppil_insert before insert on perustetyyppi for each row execute procedure update_created() ;
create trigger perustetyyppim_insert before insert on perustetyyppi for each row execute procedure update_stamp() ;
create trigger perustetyyppi_mu_update before update on perustetyyppi for each row execute procedure update_modifier() ;
create trigger perustetyyppi_cu_insert before insert on perustetyyppi for each row execute procedure update_creator() ;
create trigger perustetyyppi_mu_insert before insert on perustetyyppi for each row execute procedure update_modifier() ;

create trigger osaamisala_update before update on osaamisala for each row execute procedure update_stamp() ;
create trigger osaamisalal_insert before insert on osaamisala for each row execute procedure update_created() ;
create trigger osaamisalam_insert before insert on osaamisala for each row execute procedure update_stamp() ;
create trigger osaamisala_mu_update before update on osaamisala for each row execute procedure update_modifier() ;
create trigger osaamisala_cu_insert before insert on osaamisala for each row execute procedure update_creator() ;
create trigger osaamisala_mu_insert before insert on osaamisala for each row execute procedure update_modifier() ;


-- arviointiin liittyvät taulut

create table arvioinnin_kohdealue (
   arvioinninkohdealue_id integer NOT NULL primary key DEFAULT nextval('arvioinninkohdealue_id_seq'),
   osa varchar(6) not null references tutkinnonosa(osatunnus),
   nimi_fi text not null,
   nimi_sv text,
   jarjestys integer NOT NULL,
   muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   muutettuaika timestamptz NOT NULL,
   luotuaika timestamptz NOT NULL
);


create table ammattitaidon_kuvaus (
   ammattitaidonkuvaus_id integer NOT NULL primary key DEFAULT nextval('ammattitaidonkuvaus_id_seq'),
   arvioinninkohdealue integer NOT NULL references arvioinnin_kohdealue(arvioinninkohdealue_id),
   nimi_fi text not null,
   nimi_sv text,
   jarjestys integer NOT NULL,
   muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
   muutettuaika timestamptz NOT NULL,
   luotuaika timestamptz NOT NULL
);

create table arvio (
  tunniste varchar(16) NOT NULL primary key,
  peruste integer NOT NULL references peruste(peruste_id),
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL
);

create table arvio_tutkinnonosa (
  arviotunnus varchar(16) NOT NULL references arvio(tunniste),
  osa varchar(6) not null references tutkinnonosa(osatunnus),
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL
);

create table kohdearvio (
  arviotunnus varchar(16) NOT NULL references arvio(tunniste),
  ammattitaidon_kuvaus integer NOT NULL references ammattitaidon_kuvaus(ammattitaidonkuvaus_id),
  arvio integer, 
  kommentti text,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL,
  PRIMARY KEY (arviotunnus, ammattitaidon_kuvaus),
  CONSTRAINT arvosana_rajat CHECK ((arvio < 5) and (arvio > 0))
);

create table eperusteet_log (
  id integer NOT NULL primary key DEFAULT nextval('eperusteet_log_id_seq'),
  paivitetty timestamptz NOT NULL,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL
);

create trigger arvioinnin_kohdealue_update before update on arvioinnin_kohdealue for each row execute procedure update_stamp() ;
create trigger arvioinnin_kohdealuel_insert before insert on arvioinnin_kohdealue for each row execute procedure update_created() ;
create trigger arvioinnin_kohdealuem_insert before insert on arvioinnin_kohdealue for each row execute procedure update_stamp() ;
create trigger arvioinnin_kohdealue_mu_update before update on arvioinnin_kohdealue for each row execute procedure update_modifier() ;
create trigger arvioinnin_kohdealue_cu_insert before insert on arvioinnin_kohdealue for each row execute procedure update_creator() ;
create trigger arvioinnin_kohdealue_mu_insert before insert on arvioinnin_kohdealue for each row execute procedure update_modifier() ;

create trigger ammattitaidon_kuvaus_update before update on ammattitaidon_kuvaus for each row execute procedure update_stamp() ;
create trigger ammattitaidon_kuvausl_insert before insert on ammattitaidon_kuvaus for each row execute procedure update_created() ;
create trigger ammattitaidon_kuvausm_insert before insert on ammattitaidon_kuvaus for each row execute procedure update_stamp() ;
create trigger ammattitaidon_kuvaus_mu_update before update on ammattitaidon_kuvaus for each row execute procedure update_modifier() ;
create trigger ammattitaidon_kuvaus_cu_insert before insert on ammattitaidon_kuvaus for each row execute procedure update_creator() ;
create trigger ammattitaidon_kuvaus_mu_insert before insert on ammattitaidon_kuvaus for each row execute procedure update_modifier() ;

create trigger arvio_tutkinnonosa_update before update on arvio_tutkinnonosa for each row execute procedure update_stamp() ;
create trigger arvio_tutkinnonosal_insert before insert on arvio_tutkinnonosa for each row execute procedure update_created() ;
create trigger arvio_tutkinnonosam_insert before insert on arvio_tutkinnonosa for each row execute procedure update_stamp() ;
create trigger arvio_tutkinnonosa_mu_update before update on arvio_tutkinnonosa for each row execute procedure update_modifier() ;
create trigger arvio_tutkinnonosa_cu_insert before insert on arvio_tutkinnonosa for each row execute procedure update_creator() ;
create trigger arvio_tutkinnonosa_mu_insert before insert on arvio_tutkinnonosa for each row execute procedure update_modifier() ;

create trigger arvio_update before update on arvio for each row execute procedure update_stamp() ;
create trigger arviol_insert before insert on arvio for each row execute procedure update_created() ;
create trigger arviom_insert before insert on arvio for each row execute procedure update_stamp() ;
create trigger arvio_mu_update before update on arvio for each row execute procedure update_modifier() ;
create trigger arvio_cu_insert before insert on arvio for each row execute procedure update_creator() ;
create trigger arvio_mu_insert before insert on arvio for each row execute procedure update_modifier() ;


create trigger kohdearvio_update before update on kohdearvio for each row execute procedure update_stamp() ;
create trigger kohdearviol_insert before insert on kohdearvio for each row execute procedure update_created() ;
create trigger kohdearviom_insert before insert on kohdearvio for each row execute procedure update_stamp() ;
create trigger kohdearvio_mu_update before update on kohdearvio for each row execute procedure update_modifier() ;
create trigger kohdearvio_cu_insert before insert on kohdearvio for each row execute procedure update_creator() ;
create trigger kohdearvio_mu_insert before insert on kohdearvio for each row execute procedure update_modifier() ;

create trigger eperusteet_log_update before update on eperusteet_log for each row execute procedure update_stamp() ;
create trigger eperusteet_logl_insert before insert on eperusteet_log for each row execute procedure update_created() ;
create trigger eperusteet_logm_insert before insert on eperusteet_log for each row execute procedure update_stamp() ;
create trigger eperusteet_log_mu_update before update on eperusteet_log for each row execute procedure update_modifier() ;
create trigger eperusteet_log_cu_insert before insert on eperusteet_log for each row execute procedure update_creator() ;
create trigger eperusteet_log_mu_insert before insert on eperusteet_log for each row execute procedure update_modifier() ;

-- dokumentaatiota


COMMENT ON TABLE koulutusala      IS 'Tilastokeskuksen luokittelun mukainen koulutusala';
COMMENT ON TABLE ohje  IS 'Käyttöohjeet, jotka näkyvät käyttöliittymässä';
COMMENT ON TABLE opintoala  IS 'Tilastokeskuksen luokittelun mukainen opintoala.';

COMMENT ON TABLE peruste IS 'Tutkinnon peruste. Peruste määrää tutkinnonosat ja suorituskriteerit tutkinnolle.';
COMMENT ON COLUMN peruste.eperustetunnus IS 'ePerusteet järjestelmän käyttämä id-numero tutkinnon perusteesta.';
COMMENT ON COLUMN peruste.diaarinumero IS 'Perustemääräyksen diaarinumero. Yksilöi perusteen sisällön.';
COMMENT ON TABLE tutkinnonosa_ja_peruste IS 'Liitostaulu. Tutkinnon osa voi olla vapaaehtoinen toisessa tutkinnossa ja pakollinen toisessa.';
COMMENT ON TABLE arvio IS 'Osaamisarvio. Arviolle annetaan tunniste, jota käytetään tarvittaessa myös tiedon lataamiseen.';
COMMENT ON TABLE arvioinnin_kohdealue IS 'Arvioitavat asiat jakautuvat eri kohdealueille. Kohdealue lähinnä otsikoi arvioinnin kohteet.';
COMMENT ON TABLE ammattitaidon_kuvaus IS 'Yksittäinen arvioitava osaamisen osa-alue.';
COMMENT ON COLUMN kohdearvio.arvio IS 'Arvio numeerisena arvosanana [1-4]. Eri tasojen kriteerit määritelty perusteissa. Null tulkitaan tarkoittavan "en osaa sanoa"';
COMMENT ON COLUMN kohdearvio.kommentti IS 'Vapaamuotoinen kommentti arvioitavaan asiaan tai arvosanaan liittyen. Käyttäjä syöttää halutessaan.';
COMMENT ON COLUMN arvio.tunniste IS 'Osaamisarvion yksilöivä tunnistekoodi.';
COMMENT ON COLUMN ammattitaidon_kuvaus.jarjestys IS '>= 0. Järjestysnumero on päätelty integraatiossa ePerusteet järjestelmän rajapinnan kautta.';
COMMENT ON COLUMN arvioinnin_kohdealue.jarjestys IS '>= 0. Järjestysnumero on päätelty integraatiossa ePerusteet järjestelmän rajapinnan kautta.';
COMMENT ON TABLE arvio_tutkinnonosa IS 'Arvioon voi liittyä tutkinnonosia keskeneräisen arvioinnin tapauksessa siten että yhtään arviointia ei ole vielä annettu.';
COMMENT ON COLUMN arvio.peruste IS 'Tutkinnon perustetta ei voi päätellä suoraan tutkinnonosien kautta yksikäsitteisesti.';

-- dataa

insert into perustetyyppi(tunnus, kuvaus) values
  ('ops', 'Ammatillisena peruskoulutuksena järjestettävä ammatillinen perustutkinto (ent. opintosuunnitelmaperusteinen)'),
  ('naytto', 'Näyttötutkintona suoritettavan tutkinnon peruste');


insert into tutkintotaso(nimi, kuvaus) values
  ('erikoisammattitutkinto', 'erikoisammattitutkinto'),
  ('ammattitutkinto', 'ammattitutkinto'),
  ('perustutkinto', 'perustutkinto');

insert into tutkintotyyppi (tyyppi, selite_fi, selite_sv) values
  ('01','Yleissivistävä koulutus','Allmänbildande utbildning'),
  ('02','Ammatilliset perustutkinnot','Yrkesinriktade grundexamina'),
  ('03','Tutkintoon johtava ammatillinen lisäkoulutus','Yrkesinriktad tilläggsutbildning som leder till examen'),
  ('04','Ei tutkintoon johtava ammatillinen lisäkoulutus', 'Yrkesinriktad tilläggsutbildning som inte leder till examen'),
  ('06','Ammattikorkeakoulutus','Yrkeshögskoleutbildning'),
  ('07','Ammattikorkeakoulujen erikoistumisopinnot','Yrkeshögskolornas specialiseringsstudier'),
  ('08','Vapaan sivistystyön koulutus','Utbildning inom fritt bildningsarbete'),
  ('09','Muu ammatillinen koulutus','Övrig yrkesinriktad utbildning'),
  ('10','Muu koulutus','Övrig utbildning'),
  ('11','Kokeilu','Försök'),
  ('12','Ylempi ammattikorkeakoulututkinto','Högre yrkeshögskoleexaman'),
  ('13','Yliopistotutkinto','Universitetsexamen');

-- eheysrajoitteita, tutkinnon arviointiin liittyen

create unique index aka_yksikasitteinen_jarjestys on arvioinnin_kohdealue(osa, jarjestys);
create unique index ak_yksikasitteinen_jarjestys on ammattitaidon_kuvaus(arvioinninkohdealue, jarjestys);

-- eheysrajoitteita tutkintoihin liittyen
create unique index t_osa_yksikasitteinen_jarjestys on tutkinnonosa_ja_peruste(osa, peruste, jarjestys, pakollinen);

-- Ohjeteksti
insert into ohje (ohjetunniste, teksti_fi, teksti_sv)  values
  ('etusivu', E'Quis confluxus hodie Academicorum? \n E longinquo convenerunt \n Protinusque successerunt \n In commune forum.',
  E'Också på svenska. \n Haaluviippan ja haluviluvei. \n Vaapulavissun viipulavassun. \n Gaudeamus igitur. \n Ölökytä mäläkytä, ölökytä mäkkää.');

