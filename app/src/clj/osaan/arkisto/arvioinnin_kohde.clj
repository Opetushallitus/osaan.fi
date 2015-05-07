;; Copyright (c) 2015 The Finnish National Board of Education - Opetushallitus
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

(ns osaan.arkisto.arvioinnin-kohde
  (:require [korma.core :as sql]))

(defn ^:private hae-kohdealueet
  [tutkinnonosatunnus]
  (sql/select :arvioinnin_kohdealue
    (sql/fields :arvioinninkohdealue_id :nimi_fi :nimi_sv)
    (sql/where {:osa tutkinnonosatunnus})
    (sql/order :jarjestys :ASC)))

(defn ^:private hae-kohteet
  [arvioinninkohdealue_id]
  (sql/select :arvioinnin_kohde
    (sql/fields :arvioinninkohde_id :nimi_fi :nimi_sv)
    (sql/where {:arvioinninkohdealue arvioinninkohdealue_id})
    (sql/order :jarjestys :ASC)))

(defn hae-kohdealueet-kohteineen
  [tutkinnonosatunnus]
  (let [kohdealueet (hae-kohdealueet tutkinnonosatunnus)]
    (for [kohdealue kohdealueet]
      (assoc kohdealue :kohteet (hae-kohteet (:arvioinninkohdealue_id kohdealue))))))
