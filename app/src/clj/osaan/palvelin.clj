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

(ns osaan.palvelin
  (:gen-class)
  (:require [cheshire.generate :as json-gen]
            [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [compojure.core :as c]
            [org.httpkit.server :as hs]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.x-headers :refer [wrap-frame-options]]
            [ring.util.response :as resp]
            schema.core

            [osaan.asetukset :refer [asetukset oletusasetukset hae-asetukset konfiguroi-lokitus] :rename {asetukset asetukset-promise}]
            [osaan.infra.status :refer [build-id]]))

(schema.core/set-fn-validation! true)

(defn service-url [asetukset]
  (let [base-url (get-in asetukset [:server :base-url])
        port (get-in asetukset [:server :port])]
    (cond
      (empty? base-url) (str "http://localhost:" port "/")
      (.endsWith base-url "/") base-url
      :else (str base-url "/"))))

(defn sammuta [palvelin]
  ((:sammuta palvelin)))

(defn wrap-expires [handler]
  (fn [request]
    (assoc-in (handler request) [:headers "Expires"] "-1")))

(defn app
  "Ring-wrapperit ja compojure-reitit ilman HTTP-palvelinta"
  [asetukset]
  (require 'osaan.reitit)
  (let [reitit ((eval 'osaan.reitit/reitit) asetukset)
          _ (json-gen/add-encoder org.joda.time.LocalDate
              (fn [c json-generator]
                (.writeString json-generator (.toString c "yyyy-MM-dd"))))]
    (-> reitit
      wrap-keyword-params
      wrap-json-params
      (wrap-resource "public/app")
      wrap-params
      wrap-content-type
      wrap-not-modified
      wrap-expires
      (wrap-frame-options :deny))))

(defn ^:integration-api kaynnista-eraajon-ajastimet! [asetukset])

(defn ^:integration-api kaynnista! [alkuasetukset]
  (try
    (log/info "Käynnistetään osaan.fi, versio " "@build-id") ;; TODO
    (let [asetukset (hae-asetukset alkuasetukset)
          _ (deliver asetukset-promise asetukset)
          _ (konfiguroi-lokitus asetukset)
          sammuta (hs/run-server (app asetukset)
                                 {:port (get-in asetukset [:server :port])})]
      (when (or (not (:development-mode asetukset))
                (:eraajo asetukset))
        (kaynnista-eraajon-ajastimet! asetukset))
      (log/info "Palvelin käynnistetty:" (service-url asetukset))
      {:sammuta sammuta})
    (catch Throwable t
      (let [virheviesti "Palvelimen käynnistys epäonnistui"]
        (log/error t virheviesti)
        (binding [*out* *err*]
          (println virheviesti))
        (.printStackTrace t *err*)
        (System/exit 1)))))

(defn -main []
  (kaynnista! oletusasetukset))
