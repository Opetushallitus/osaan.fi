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
            [osaan.compojure-util :as cu]
            [osaan.skeema :as skeema]))

(def tutkinnot '({:opintoala_tkkoodi "509", :nimi_sv "Billackerare (grundexamen)", :opintoala_nimi_fi "Ajoneuvo- ja kuljetustekniikka", :nimi_fi "Automaalari (perustutkinto)", :opintoala_nimi_sv "Fordons- och transportteknik", :tutkintotaso "perustutkinto", :tutkintotunnus "351304"} {:opintoala_tkkoodi "509", :nimi_sv "Specialyrkesexamen för billackerarmästare", :opintoala_nimi_fi "Ajoneuvo- ja kuljetustekniikka", :nimi_fi "Automaalarimestarin erikoisammattitutkinto", :opintoala_nimi_sv "Fordons- och transportteknik", :tutkintotaso "erikoisammattitutkinto", :tutkintotunnus "357302"} {:opintoala_tkkoodi "509", :nimi_sv "Yrkesexamen för billackerare", :opintoala_nimi_fi "Ajoneuvo- ja kuljetustekniikka", :nimi_fi "Automaalarin ammattitutkinto", :opintoala_nimi_sv "Fordons- och transportteknik", :tutkintotaso "ammattitutkinto", :tutkintotunnus "354307"}))

(c/defroutes reitit
  (cu/defapi :julkinen nil :get "/" [nimi opintoala]
    (json-response tutkinnot [skeema/Tutkinto])))
