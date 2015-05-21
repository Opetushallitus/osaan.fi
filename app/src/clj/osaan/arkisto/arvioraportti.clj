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

(defn eos-tulkinta [v]
  (or v "en osaa sanoa"))

(def arvosanat
  {:fi {1 "En osaa"
        2 "Osaan hiukan"
        3 "Osaan melko hyvin"
        4 "Osaan hyvin"
        nil "En osaa sanoa"}})

(defn arvosanatulkinta [v kieli]
  {:post [(not (nil? %))]}
  (get (kieli arvosanat) v))

(defn hae
  [arviotunnus]
  (let [tulos (sql/select :kohdearvio
                (sql/join :inner :ammattitaidon_kuvaus (= :ammattitaidon_kuvaus.ammattitaidonkuvaus_id :kohdearvio.ammattitaidon_kuvaus))
                (sql/join :inner :arvioinnin_kohdealue (= :arvioinnin_kohdealue.arvioinninkohdealue_id :ammattitaidon_kuvaus.arvioinninkohdealue))
                (sql/fields :arvio :kommentti :ammattitaidon_kuvaus.ammattitaidonkuvaus_id :arvioinnin_kohdealue.arvioinninkohdealue_id
                            :ammattitaidon_kuvaus.arvioinninkohdealue :arvioinnin_kohdealue.osa :ammattitaidon_kuvaus.nimi_fi :ammattitaidon_kuvaus.nimi_sv :ammattitaidon_kuvaus.jarjestys
                            [:arvioinnin_kohdealue.nimi_fi :aka_nimi_fi] [:arvioinnin_kohdealue.nimi_sv :aka_nimi_sv]
                            [:arvioinnin_kohdealue.jarjestys :aka_jarjestys])
                (sql/where (= :arviotunnus arviotunnus))
                (sql/order :arvioinnin_kohdealue.osa :ASC)
                (sql/order :aka_jarjestys :ASC)
                (sql/order :ammattitaidon_kuvaus.jarjestys :ASC))]
    (if (empty? tulos)
      nil
      tulos)))

(defn tulkitse-arvosanat [arvio kieli]
  (map #(update-in % [:arvio] arvosanatulkinta kieli) arvio))
