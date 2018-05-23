
-- Merkitään vanhat opintoalat ja koulutusalat päättyneiksi
update opintoala set voimassa_loppupvm = to_date('01-07-2017', 'DD-MM-YYYY');
update koulutusala set voimassa_loppupvm = to_date('01-07-2017', 'DD-MM-YYYY');

-- Testitapausten kannalta tietokanta on tyhjä ja insert ei tapahdu. Asennuksessa olemassaolevan tietokannan päälle tapahtuu.
insert into opintoala(opintoalatunnus, koulutusala, nimi_fi, voimassa_alkupvm, voimassa_loppupvm)
  select '007', koulutusalatunnus, 'Keksitty opintoala ISCED-päivitystä varten', to_date('2017-01-01','YYYY-MM-DD'), to_date('2017-01-01','YYYY-MM-DD')
  from koulutusala where koulutusalatunnus = '0' on conflict do nothing;

update tutkinto set opintoala = '007' where tutkintotunnus not in
('321603','377111','371113','354116','351108','354204','010001','324503');

delete from opintoala o where opintoalatunnus not in ('505','502','706','703','007', '021', '071');

delete from koulutusala k where not exists (select null from opintoala o where o.koulutusala = k.koulutusalatunnus);
