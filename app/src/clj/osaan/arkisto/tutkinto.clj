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

(ns osaan.arkisto.tutkinto
  (:require [korma.core :as sql]
            [oph.korma.common :as sql-util]
            [osaan.infra.sql.korma :as taulut]
            [oph.common.util.util :refer [pvm-mennyt-tai-tanaan? pvm-tuleva-tai-tanaan?]]))

(defn ^:integration-api lisaa!
  [tiedot]
  (sql/insert taulut/tutkinto
    (sql/values tiedot)))

(defn ^:integration-api paivita!
  [tutkintotunnus tiedot]
  (sql-util/update-unique taulut/tutkinto
    (sql/set-fields tiedot)
    (sql/where {:tutkintotunnus tutkintotunnus})))

(defn hae-kaikki
  []
  (sql/select taulut/tutkinto
    (sql/order :tutkintotunnus)))

(defn hae
  [nimi opintoala]
  (let [nimi (str "%" nimi "%")]
    (->
      (sql/select* :tutkinto)
      (sql/join :opintoala (= :opintoala.opintoalatunnus :opintoala))
      (sql/join :peruste (= :peruste.tutkinto :tutkintotunnus))
      (sql/fields :tutkintotunnus :nimi_fi :nimi_sv [:opintoala.nimi_fi :opintoala_nimi_fi] [:opintoala.nimi_sv :opintoala_nimi_sv] [:peruste.diaarinumero :peruste_diaarinumero])
      (sql/where (or {:nimi_fi [sql-util/ilike nimi]}
                     {:nimi_sv [sql-util/ilike nimi]}))
      (cond->
        opintoala (sql/where {:opintoala opintoala}))
      sql/exec)))
