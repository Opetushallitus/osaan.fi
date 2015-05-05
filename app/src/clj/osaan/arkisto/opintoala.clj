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

(ns osaan.arkisto.opintoala
  (:require [korma.core :as sql]
            [osaan.infra.sql.korma :as taulut]))

(defn ^:integration-api lisaa!
  [tiedot]
  (sql/insert taulut/opintoala
    (sql/values tiedot)))

(defn ^:integration-api paivita!
  [opintoalatunnus tiedot]
  (sql/update taulut/opintoala
    (sql/set-fields tiedot)
    (sql/where {:opintoalatunnus opintoalatunnus})))

(defn hae-kaikki
  []
  (sql/select taulut/opintoala
    (sql/order :opintoalatunnus)))

(defn hae
  [opintoalatunnus]
  (first
    (sql/select taulut/opintoala
      (sql/where {:opintoalatunnus opintoalatunnus}))))
