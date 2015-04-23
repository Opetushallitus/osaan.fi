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

(ns osaan.sql.test-util
  (:import java.util.Locale)
  (:require [korma.core :as sql]
            [korma.db :as db]
            [oph.common.infra.i18n :as i18n]
            [oph.korma.common]
            [osaan.asetukset :refer [hae-asetukset oletusasetukset]]
            [osaan.infra.kayttaja.sql :refer [with-sql-kayttaja]]))

(def testikayttaja-oid "SAMPO")
(def testi-locale (Locale. "fi"))

(defn alusta-korma!
  ([asetukset]
    (let [db-asetukset (merge-with #(or %2 %1)
                         (:db asetukset)
                         {:host (System/getenv "OSAAN_DB_HOST")
                          :port (System/getenv "OSAAN_DB_PORT")
                          :name (System/getenv "OSAAN_DB_NAME")
                          :user (System/getenv "OSAAN_DB_USER")
                          :password (System/getenv "OSAAN_DB_PASSWORD")})]
      (oph.korma.common/luo-db db-asetukset)))
    ([]
    (let [dev-asetukset (assoc oletusasetukset :development-mode true)
          asetukset (hae-asetukset dev-asetukset)]
      (alusta-korma! asetukset))))

(defn tietokanta-fixture-oid
  "Annettu käyttäjätunnus sidotaan Kormalle testifunktion ajaksi. Sitoo myös localen testin ajaksi."
  [f oid]
  (let [pool (alusta-korma!)]
    (with-sql-kayttaja oid
      (binding [i18n/*locale* testi-locale]
        (try
          (f)
          (finally
            (-> pool :pool :datasource .close)))))))

(defn tietokanta-fixture [f]
  (tietokanta-fixture-oid f testikayttaja-oid))

(defn exec-raw-fixture
  "Alustaa korman ennen testiä ja sulkee tietokantayhteydet testin jälkeen."
  [f]
  (let [pool (alusta-korma!)]
    (try
      (f)
      (finally
        (-> pool :pool :datasource .close)))))
