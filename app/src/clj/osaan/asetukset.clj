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

(ns osaan.asetukset
  (:require [clojure.java.io :refer [file]]
            [clojure.tools.logging :as log]
            [schema.core :as s]

            [oph.common.infra.asetukset :refer [lue-asetukset]])
  (:import [ch.qos.logback.classic.joran JoranConfigurator]
           [org.slf4j LoggerFactory]))

(def Asetukset
  {:server {:port s/Int
            :base-url s/Str}
   :db {:host s/Str
        :port s/Int
        :name s/Str
        :user s/Str
        :password s/Str
        :maximum-pool-size s/Int
        :minimum-pool-size s/Int}
   :eperusteet-palvelu {:url s/Str}
   :koodistopalvelu {:url s/Str}
   :eraajo Boolean
   :development-mode Boolean
   :logback {:properties-file s/Str}
   :ajastus {:eperusteet s/Str
             :koodistopalvelu s/Str}})

(def oletusasetukset
  {:server {:port 8084
            :base-url ""}
   :db {:host "127.0.0.1"
        :port 4567
        :name "osaan"
        :user "osaan_user"
        :password "osaan"
        :maximum-pool-size 15
        :minimum-pool-size 3}
   :eperusteet-palvelu {:url "https://virkailija.opintopolku.fi/eperusteet-service/"}
   :eraajo false
   :koodistopalvelu {:url "https://virkailija.opintopolku.fi/koodisto-service/rest/json/"}
   :development-mode false ; oletusarvoisesti ei olla kehitysmoodissa. Pitää erikseen kääntää päälle jos tarvitsee kehitysmoodia.
   :logback {:properties-file "resources/logback.xml"}
   :ajastus {:eperusteet "0 15 4 * * ?"
             :koodistopalvelu "0 15 3 * * ?"}})

(defn hae-asetukset
  ([alkuasetukset] (lue-asetukset alkuasetukset Asetukset "osaan.properties"))
  ([] (hae-asetukset oletusasetukset)))

(defn service-path [base-url]
  (let [path (drop 3 (clojure.string/split base-url #"/"))]
    (str "/" (clojure.string/join "/" path))))

(defn konfiguroi-lokitus
  "Konfiguroidaan logback asetukset tiedostosta."
  [asetukset]
  (let [filepath (-> asetukset :logback :properties-file)
        config-file (file filepath)
        config-file-path (.getAbsolutePath config-file)
        configurator (JoranConfigurator.)
        context (LoggerFactory/getILoggerFactory)]
    (log/info "logback configuration reset: " config-file-path)
    (.setContext configurator context)
    (.reset context)
    (.doConfigure configurator config-file-path)))
