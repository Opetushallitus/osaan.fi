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

(ns osaan.arkisto.koulutusala
  (:require [korma.core :as sql]
            [osaan.infra.sql.korma :as taulut]))

(defn ^:integration-api lisaa!
  [tiedot]
  (sql/insert taulut/koulutusala
    (sql/values tiedot)))

(defn ^:integration-api paivita!
  [koulutusalatunnus tiedot]
  (sql/update taulut/koulutusala
    (sql/set-fields tiedot)
    (sql/where {:koulutusalatunnus koulutusalatunnus})))

(defn hae-kaikki
  []
  (sql/select taulut/koulutusala
    (sql/order :koulutusalatunnus)))

(defn hae
  [koulutusalatunnus]
  (first
    (sql/select taulut/koulutusala
      (sql/where {:koulutusalatunnus koulutusalatunnus}))))

(defn ^:private hae-koulutusalat
  []
  (sql/select :koulutusala
    (sql/fields :koulutusalatunnus :nimi_fi :nimi_sv)
    (sql/order :koulutusalatunnus)))

(defn ^:private hae-opintoalat
  []
  (sql/select :opintoala
    (sql/fields :opintoalatunnus :koulutusala :nimi_fi :nimi_sv)
    (sql/where (sql/sqlfn "exists" (sql/subselect :peruste
                                     (sql/join :inner :tutkinto {:tutkinto.tutkintotunnus :peruste.tutkinto})
                                     (sql/where {:tutkinto.opintoala :opintoala.opintoalatunnus}))))))

(defn hae-koulutusalat-opintoaloilla
  []
  (let [koulutusalat (hae-koulutusalat)
        opintoalat (hae-opintoalat)
        opintoalat-by-koulutusalatunnus (group-by :koulutusala opintoalat)]
    (for [koulutusala koulutusalat
          :let [koulutusalatunnus (:koulutusalatunnus koulutusala)
                koulutusalan-opintoalat (get opintoalat-by-koulutusalatunnus koulutusalatunnus)
                koulutusalan-opintoalat (map #(dissoc % :koulutusala) koulutusalan-opintoalat)]
          :when (seq koulutusalan-opintoalat)]
      (assoc koulutusala :opintoalat koulutusalan-opintoalat))))
