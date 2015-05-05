;; Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
;;
;; This program is free software:  Licensed under the EUPL, Version 1.1 or - as
;; soon as they will be approved by the European Commission - subsequent versions
;; of the EUPL (the "Licence");
;;
;; You may not use this work except in compliance with the Licence.
;; You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
;;
;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; European Union Public Licence for more details.

(ns osaan.arkisto.arvioraportti
  (:require [korma.core :as sql]))

(defn hae-arvio
  [arviotunnus]
  (sql/select :kohdearvio
    (sql/join :inner :arvioinnin_kohde (= :arvioinnin_kohde.arvioinninkohde_id :kohdearvio.arviokohde))
    (sql/join :inner :arvioinnin_kohdealue (= :arvioinnin_kohdealue.arvioinninkohdealue_id :arvioinnin_kohde.arvioinninkohdealue))
    (sql/fields :arvio
                :arvioinnin_kohde.arvioinninkohdealue :arvioinnin_kohdealue.osa :arvioinnin_kohde.nimi_fi :arvioinnin_kohde.nimi_sv :arvioinnin_kohde.jarjestys
                [:arvioinnin_kohdealue.nimi_fi :aka_nimi_fi] [:arvioinnin_kohdealue.nimi_sv :aka_nimi_sv]
                [:arvioinnin_kohdealue.jarjestys :aka_jarjestys])
    (sql/where (= :arviotunnus arviotunnus))
    (sql/order :arvioinnin_kohdealue.osa :ASC)
    (sql/order :aka_jarjestys :ASC)
    (sql/order :arvioinnin_kohde.jarjestys :ASC)
    ))

;select ka.arvio, ak.nimi_fi, ak.nimi_sv, ak.jarjestys, aka.osa, aka.nimi_fi as aka_nimi_fi, aka.nimi_sv as aka_nimi_sv, aka.jarjestys as aka_jarjestys from kohdearvio ka
 ; inner join arvioinnin_kohde ak on ak.arvioinninkohde_id = ka.arviokohde
 ; inner join arvioinnin_kohdealue aka on aka.arvioinninkohdealue_id = ak.arvioinninkohdealue
 ; where ka.arviotunnus = 'testiarvio'
 ; order by osa, aka_jarjestys, jarjestys;