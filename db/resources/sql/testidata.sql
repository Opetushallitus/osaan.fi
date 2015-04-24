set session osaan.kayttaja='JARJESTELMA';

insert into kayttaja(oid, etunimi, sukunimi, voimassa, muutettuaika, luotuaika, luotu_kayttaja, muutettu_kayttaja)
values ('SAMPO', 'Seppo', 'Ilmarinen', true, current_timestamp, current_timestamp, 'JARJESTELMA', 'JARJESTELMA');

insert into koulutusala(koulutusala_tkkoodi, nimi_fi,nimi_sv, voimassa_alkupvm) values ('6', 'Kulttuuriala', 'Kultur', to_date('1997-01-01', 'YYYY-MM-DD'));
insert into opintoala (opintoala_tkkoodi, koulutusala_tkkoodi, nimi_fi, nimi_sv, voimassa_alkupvm)
 values ('202', '6', 'Viestintä ja informaatiotieteet', 
 'Mediekultur och informationsvetenskaper',
 to_date('1997-01-01', 'YYYY-MM-DD'));
 
 insert into tutkinto(tutkintotunnus, opintoala, nimi_fi,voimassa_alkupvm, tutkintotaso)
   values ('324601', '202', 'Audiovisuaalisen viestinnän ammattitutkinto',
   to_date('1997-01-01', 'YYYY-MM-DD'),
   'ammattitutkinto');

insert into tutkinnonosa(osatunnus, nimi_fi,tutkinto) values
  ('100001','Audiovisuaalinen tuotanto', '324601'),
  ('100002','Video- ja elokuvatuotanto', '324601'),
  ('100003','Televisiotuotanto', '324601');

insert into tutkinto_ja_tutkinnonosa (tutkinto, tutkinnonosa, jarjestys) values
  ('324601','100001',1),
  ('324601','100002',2),
  ('324601','100003',3);

insert into peruste (diaarinumero, alkupvm, tutkinto, tyyppi)
 values ('41/011/2005', to_date('2005-01-01', 'YYYY-MM-DD'), '324601', 'naytto');

insert into tutkinnonosa_ja_peruste(osa, peruste, pakollinen)
values ('100001', '41/011/2005', true),
 ('100002', '41/011/2005', true),
 ('100003', '41/011/2005', true);

insert into arvioinnin_kohdealue(arvioinninkohdealue_id, osa, nimi_fi, nimi_sv, jarjestys)
 values
   (-1, '100001', '1. Työprosessin hallinta', 'Behärskande av arbetsprocessen', 1),
   (-2, '100001', '2. Työmenetelmien, -välineiden ja materiaalin hallinta', 'Behärskande av arbetsmetoder, -redskap och material', 2);

insert into arvioinnin_kohde(arvioinninkohdealue, nimi_fi, nimi_sv, jarjestys)
  values
    (-1, 'Esituotanto','Förproduktion', 1),
    (-1, 'Tuotanto', 'Produktion', 2), 
    (-1, 'Jälkituotanto', 'Efterproduktion', 3),
    (-2, 'Oman työn suunnittelu', 'Planering av det egna arbetet', 1),
    (-2, 'Aineiston tuottaminen', 'Produktion av material', 2);
