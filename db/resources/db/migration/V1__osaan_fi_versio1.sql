set session osaan.kayttaja='JARJESTELMA';

CREATE SEQUENCE arvioinninkohde_id_seq;
CREATE SEQUENCE arvioinninkohdealue_id_seq;

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
    opintoala_tkkoodi varchar(3) not null primary key,
    koulutusala_tkkoodi varchar(3) not null references koulutusala(koulutusala_tkkoodi),
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
   
   nimi_fi text not null,
   nimi_sv text ,
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

create table tutkinto_ja_tutkinnonosa(
  tutkinto varchar(6) references tutkinto(tutkintotunnus), 
  tutkinnonosa varchar(6) references tutkinnonosa(osatunnus),
  jarjestysnumero int not null,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamp NOT NULL,
  luotuaika timestamp NOT NULL,
  PRIMARY KEY(tutkinto, tutkinnonosa)
);

create trigger tutkinto_ja_tutkinnonosa_update before update on tutkinto_ja_tutkinnonosa for each row execute procedure update_stamp() ;
create trigger tutkinto_ja_tutkinnonosal_insert before insert on tutkinto_ja_tutkinnonosa for each row execute procedure update_created() ;
create trigger tutkinto_ja_tutkinnonosam_insert before insert on tutkinto_ja_tutkinnonosa for each row execute procedure update_stamp() ;
create trigger tutkinto_ja_tutkinnonosa_mu_update before update on tutkinto_ja_tutkinnonosa for each row execute procedure update_modifier() ;
create trigger tutkinto_ja_tutkinnonosa_mu_insert before insert on tutkinto_ja_tutkinnonosa for each row execute procedure update_modifier() ;
create trigger tutkinto_ja_tutkinnonosa_cu_insert before insert on tutkinto_ja_tutkinnonosa for each row execute procedure update_creator() ;

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

create table arvioinnin_kohde (
   arvioinninkohde_id integer NOT NULL primary key DEFAULT nextval('arvioinninkohde_id_seq'),
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
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL  
);

create table kohdearvio (
  arviotunnus varchar(16) NOT NULL references arvio(tunniste),
  arviokohde integer NOT NULL references arvioinnin_kohde(arvioinninkohde_id),
  arvio integer NOT NULL,
  muutettu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  luotu_kayttaja varchar(80) NOT NULL references kayttaja(oid),
  muutettuaika timestamptz NOT NULL,
  luotuaika timestamptz NOT NULL,
  PRIMARY KEY (arviotunnus, arviokohde)
);

create trigger arvioinnin_kohdealue_update before update on arvioinnin_kohdealue for each row execute procedure update_stamp() ;
create trigger arvioinnin_kohdealuel_insert before insert on arvioinnin_kohdealue for each row execute procedure update_created() ;
create trigger arvioinnin_kohdealuem_insert before insert on arvioinnin_kohdealue for each row execute procedure update_stamp() ;
create trigger arvioinnin_kohdealue_mu_update before update on arvioinnin_kohdealue for each row execute procedure update_modifier() ;
create trigger arvioinnin_kohdealue_cu_insert before insert on arvioinnin_kohdealue for each row execute procedure update_creator() ;
create trigger arvioinnin_kohdealue_mu_insert before insert on arvioinnin_kohdealue for each row execute procedure update_modifier() ;

create trigger arvioinnin_kohde_update before update on arvioinnin_kohde for each row execute procedure update_stamp() ;
create trigger arvioinnin_kohdel_insert before insert on arvioinnin_kohde for each row execute procedure update_created() ;
create trigger arvioinnin_kohdem_insert before insert on arvioinnin_kohde for each row execute procedure update_stamp() ;
create trigger arvioinnin_kohde_mu_update before update on arvioinnin_kohde for each row execute procedure update_modifier() ;
create trigger arvioinnin_kohde_cu_insert before insert on arvioinnin_kohde for each row execute procedure update_creator() ;
create trigger arvioinnin_kohde_mu_insert before insert on arvioinnin_kohde for each row execute procedure update_modifier() ;


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

-- dokumentaatiota

COMMENT ON TABLE peruste IS 'Tutkinnon peruste. Peruste määrää tutkinnonosat ja suorituskriteerit tutkinnolle.';
COMMENT ON TABLE tutkinnonosa_ja_peruste IS 'Liitostaulu. Tutkinnon osa voi olla vapaaehtoinen toisessa tutkinnossa ja pakollinen toisessa.';
COMMENT ON TABLE arvio IS 'Osaamisarvio. Arviolle annetaan tunniste, jota käytetään tarvittaessa myös tiedon lataamiseen.';
COMMENT ON TABLE arvioinnin_kohdealue IS 'Arvioitavat asiat jakautuvat eri kohdealueille. Kohdealue lähinnä otsikoi arvioinnin kohteet.';
COMMENT ON TABLE arvioinnin_kohde IS 'Yksittäinen arvioitava osaamisen osa-alue.';
COMMENT ON COLUMN kohdearvio.arvio IS 'Arvio numeerisena arvosanana (0-5). Eri tasojen kriteerit määritelty perusteissa.';
COMMENT ON COLUMN arvio.tunniste IS 'Osaamisarvion yksilöivä tunnistekoodi.';
COMMENT ON COLUMN arvioinnin_kohde.jarjestys IS '>= 0. Järjestysnumero on päätelty integraatiossa ePerusteet järjestelmän rajapinnan kautta.';
COMMENT ON COLUMN arvioinnin_kohdealue.jarjestys IS '>= 0. Järjestysnumero on päätelty integraatiossa ePerusteet järjestelmän rajapinnan kautta.';

-- dataa

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






