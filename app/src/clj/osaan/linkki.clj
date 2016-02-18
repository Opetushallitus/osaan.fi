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

(ns osaan.linkki
  (:require [compojure.api.core :refer [GET routes]]
            [schema.core :as s]
            [oph.common.util.http-util :refer [response-or-404]]
            [osaan.arkisto.peruste :as peruste-arkisto]
            osaan.compojure-util))

(defn reitit [asetukset]
  (routes
    (GET "/osien-valinta" []
      :kayttooikeus :julkinen
      :query-params [tutkinto :- s/Str
                     diaarinumero :- s/Str
                     tyyppi :- s/Str]
      (let [perusteid (peruste-arkisto/hae-perusteid diaarinumero tyyppi)
            url (str (-> asetukset :server :base-url) "/#/osien-valinta?tutkinto=" tutkinto "&peruste=" perusteid)]
        {:status  302
         :headers {"Location" url}}))))
