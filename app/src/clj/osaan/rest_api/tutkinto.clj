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

(ns osaan.rest-api.tutkinto
  (:require [compojure.api.core :refer [GET defroutes]]
            [schema.core :as s]
            [oph.common.util.http-util :refer [response-or-404]]
            [osaan.arkisto.tutkinto :as arkisto]
            osaan.compojure-util
            [osaan.skeema :as skeema]))

(defroutes reitit
  (GET "/peruste/:perusteid" []
    :kayttooikeus :julkinen
    :path-params [perusteid :- s/Int]
    :return skeema/Tutkinto
    (response-or-404 (arkisto/hae-perusteella perusteid)))
  (GET "/" []
    :kayttooikeus :julkinen
    :query-params [{kieli :- s/Str "fi"}
                   {nimi :- s/Str nil}
                   {opintoala :- s/Str nil}
                   {tutkintotyyppi :- s/Str nil}
                   {voimaantulevat :- Boolean false}]
    :return [skeema/TutkintoHakutulos]
    (let [tutkintotaso (if (= "kaikki" tutkintotyyppi) nil tutkintotyyppi)
          oala (if (.equals "" opintoala) nil opintoala)]
      (response-or-404 (arkisto/hae-ehdoilla nimi kieli oala tutkintotaso voimaantulevat)))))
