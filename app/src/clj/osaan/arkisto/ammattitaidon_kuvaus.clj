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

(ns osaan.arkisto.ammattitaidon-kuvaus
  (:require [korma.core :as sql]))

(defn ^:private hae-kohdealueet
  [tutkinnonosatunnus]
  (sql/select :arvioinnin_kohdealue
    (sql/fields :arvioinninkohdealue_id :nimi_fi :nimi_sv)
    (sql/where {:osa tutkinnonosatunnus})
    (sql/order :jarjestys :ASC)))

(defn ^:private hae-ammattitaidon-kuvaukset
  [arvioinninkohdealue_id]
  (sql/select :ammattitaidon_kuvaus
    (sql/fields :ammattitaidonkuvaus_id :nimi_fi :nimi_sv)
    (sql/where {:arvioinninkohdealue arvioinninkohdealue_id})
    (sql/order :jarjestys :ASC)))

(defn hae-kohdealueet-kuvauksineen
  [tutkinnonosatunnus]
  (let [kohdealueet (hae-kohdealueet tutkinnonosatunnus)]
    (for [kohdealue kohdealueet]
      (assoc kohdealue :kuvaukset (hae-ammattitaidon-kuvaukset (:arvioinninkohdealue_id kohdealue))))))
