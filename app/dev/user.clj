;; Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
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

(ns user
  (:require clojure.core.cache
            [clojure.java.shell :refer [with-sh-dir sh]]
            [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :as nsr]
            [clj-http.client :as hc]
            schema.core

            [osaan.infra.kayttaja.sql :refer [with-sql-kayttaja]]))

(schema.core/set-fn-validation! true)

(def frontend-kaannoskomennot ["npm install"
                               "bower install"
                               "grunt build"])

(defn kaanna-frontend []
  (with-sh-dir "frontend"
    (doseq [komento frontend-kaannoskomennot]
      (println "$" komento)
      (let [{:keys [err out]} (sh "bash" "-l" "-c" komento)]
        (println err out)))))

(defonce ^:private palvelin (atom nil))

(defn ^:private repl-asetukset
  "Muutetaan oletusasetuksia siten että saadaan järkevät asetukset kehitystyötä varten"
  []
  (->
    @(ns-resolve 'osaan.asetukset 'oletusasetukset)
    (assoc :development-mode true)
    (assoc-in [:server :base-url] "http://192.168.50.1:8084")))

(defn ^:private kaynnista! []
  {:pre [(not @palvelin)]
   :post [@palvelin]}
  (kaanna-frontend)
  (require 'osaan.palvelin)
  (reset! palvelin ((ns-resolve 'osaan.palvelin 'kaynnista!) (repl-asetukset))))

(defn ^:private sammuta! []
  {:pre [@palvelin]
   :post [(not @palvelin)]}
  ((ns-resolve 'osaan.palvelin 'sammuta) @palvelin)
  (reset! palvelin nil))

(defn uudelleenkaynnista! []
  (when @palvelin
    (sammuta!))
  (nsr/refresh :after 'user/kaynnista!))

(defmacro with-testikayttaja [& body]
  `(with-sql-kayttaja "JARJESTELMA"
     ~@body))
