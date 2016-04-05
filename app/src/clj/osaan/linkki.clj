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

; testicaseja: 
; parametreja puuttuu: http://localhost:8084/linkki/osien-valinta?tutkinto=324601&peruste=-2&tyyppi=naytto&diaarinumero=12
; persuste on vanha: http://localhost:8084/linkki/osien-valinta?tutkinto=324601&peruste=-1&tyyppi=naytto&diaarinumero=41/011/2005
; toimiva: http://localhost:8084/linkki/osien-valinta?tutkinto=324601&peruste=-1&tyyppi=naytto&diaarinumero=38/011/2014
; :localhost ja 127.0.0.1 voidaan kokeilla erikseen. http://127.0.0.1:8084/#/osien-valinta?tutkinto=324601&peruste=-2
(defn reitit [asetukset]
  (routes
    (GET "/osien-valinta" request
      :kayttooikeus :julkinen
      :query-params [tutkinto :- s/Str
                     diaarinumero :- s/Str
                     tyyppi :- s/Str]
      (let [perusteid (peruste-arkisto/hae-perusteid diaarinumero tyyppi)
            ;"/" (-> asetukset :server :base-url)
            url (str (name (:scheme request)) "://" (:server-name request) ":" (:server-port request) "/#/osien-valinta?tutkinto=" tutkinto "&peruste=" perusteid)]
        (if (nil? perusteid)
          ; ei löydy -> 400 (BAD REQUEST) ja virheilmoitus. Tätä ei pitäisi tapahtua käytännössä.
          {:status 400
           :headers {"Content-Type" "text/plain; charset=utf-8"}
           :body "Tutkintoa tai sen perusteita ei löydy."}
          ; jos löytyy, HTTP redirect
          {:status  302
           :headers {"Location" url}})))))
