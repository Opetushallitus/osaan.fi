set session osaan.kayttaja='JARJESTELMA';

insert into kayttaja(oid, etunimi, sukunimi, voimassa, muutettuaika, luotuaika, luotu_kayttaja, muutettu_kayttaja)
values ('SAMPO', 'Seppo', 'Ilmarinen', true, current_timestamp, current_timestamp, 'JARJESTELMA', 'JARJESTELMA');

insert into koulutusala(koulutusalatunnus, nimi_fi,nimi_sv, voimassa_alkupvm) values ('6', 'Kulttuuriala', 'Kultur', to_date('1997-01-01', 'YYYY-MM-DD'));
insert into opintoala (opintoalatunnus, koulutusala, nimi_fi, nimi_sv, voimassa_alkupvm)
 values ('202', '6', 'Viestintä ja informaatiotieteet',
 'Mediekultur och informationsvetenskaper',
 to_date('1997-01-01', 'YYYY-MM-DD'));

 insert into tutkinto(tutkintotunnus, opintoala, nimi_fi,voimassa_alkupvm, tutkintotaso)
   values ('324601', '202', 'Audiovisuaalisen viestinnän ammattitutkinto',
   to_date('1997-01-01', 'YYYY-MM-DD'),
   'ammattitutkinto');

  insert into tutkintonimike(nimiketunnus, nimi_fi, nimi_sv, tutkinto)
   values ('10092', 'Media-assistentti', 'Medieassistent', '324601');

insert into peruste (diaarinumero, alkupvm, tutkinto, tyyppi)
 values ('41/011/2005', to_date('2005-01-01', 'YYYY-MM-DD'), '324601', 'naytto');

insert into tutkinnonosa(osatunnus, nimi_fi) values
  ('100001','Audiovisuaalinen tuotanto'),
  ('100002','Video- ja elokuvatuotanto'),
  ('100003','Televisiotuotanto');

insert into tutkinnonosa_ja_peruste(osa, peruste, jarjestys, pakollinen)
values ('100001', '41/011/2005', 1, true),
 ('100002', '41/011/2005', 2, true),
 ('100003', '41/011/2005', 3, true);

insert into arvioinnin_kohdealue(arvioinninkohdealue_id, osa, nimi_fi, nimi_sv, jarjestys)
 values
   (-1, '100001', '1. Työprosessin hallinta', 'Behärskande av arbetsprocessen', 1),
   (-2, '100001', '2. Työmenetelmien, -välineiden ja materiaalin hallinta', 'Behärskande av arbetsmetoder, -redskap och material', 2);

insert into arvioinnin_kohde(arvioinninkohde_id, arvioinninkohdealue, nimi_fi, nimi_sv, jarjestys)
  values
    (-1, -1, 'Esituotanto','Förproduktion', 1),
    (-2, -1, 'Tuotanto', 'Produktion', 2), 
    (-3, -1, 'Jälkituotanto', 'Efterproduktion', 3),
    (-4, -2, 'Oman työn suunnittelu', 'Planering av det egna arbetet', 1),
    (-5, -2, 'Aineiston tuottaminen', 'Produktion av material', 2);

insert into osaamisala (osaamisalatunnus, tutkinto, nimi_fi, nimi_sv, voimassa_alkupvm, voimassa_loppupvm, versio, koodistoversio) values ('2002', '324601', 'Kuvauksen ja valaisun osaamisala', '', '2014-01-01', '2199-01-01', 1, 1);
insert into osaamisala (osaamisalatunnus, tutkinto, nimi_fi, nimi_sv, voimassa_alkupvm, voimassa_loppupvm, versio, koodistoversio) values ('2003', '324601', 'Mediatyön osaamisala', '', '2014-01-01', '2199-01-01', 1, 1);

insert into arvio (tunniste) values ('testiarvio');

insert into kohdearvio (arviotunnus, arviokohde, arvio, kommentti)
  values
    ('testiarvio', -1, 1),
    ('testiarvio', -2, 3),
    ('testiarvio', -3, 3),
    ('testiarvio', -4, 4),
    ('testiarvio', -5, null, 'En täysin ymmärtänyt miten eri osaamistasot tässä osa-alueessa tulisi arvioida.');
