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
  (:require [compojure.core :as c]
            [oph.common.util.http-util :refer [json-response]]
            [osaan.arkisto.tutkinto :as arkisto]
            [osaan.compojure-util :as cu]
            [osaan.skeema :as skeema]))

(c/defroutes reitit
  (cu/defapi :julkinen nil :get "/peruste/:perusteid" [perusteid]
    (json-response (arkisto/hae-perusteella (Integer/parseInt perusteid)) skeema/Tutkinto))

  (cu/defapi :julkinen nil :get "/" [nimi opintoala tutkintotyyppi voimaantulevat]
     (let [tutkintotaso (if (= "kaikki" tutkintotyyppi) nil tutkintotyyppi)
           oala (if (.equals "" opintoala) nil opintoala)]
       (json-response (arkisto/hae-ehdoilla nimi oala tutkintotaso (Boolean/valueOf voimaantulevat)) [skeema/TutkintoHakutulos]))))
