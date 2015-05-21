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

(ns osaan.rest-api.arvioinnin-kohde
  (:require [compojure.core :as c]
            [oph.common.util.http-util :refer [json-response]]
            [osaan.arkisto.arvioinnin-kohde :as arkisto]
            [osaan.compojure-util :as cu]
            [osaan.skeema :as skeema]))

(c/defroutes reitit
  (cu/defapi :julkinen nil :get "/alueet" [tutkinnonosatunnus]
    (let [osatunnukset (if (sequential? tutkinnonosatunnus)
                         tutkinnonosatunnus
                         [tutkinnonosatunnus])]
      (json-response (into {} (for [osatunnus osatunnukset]
                                {osatunnus (arkisto/hae-kohdealueet-kuvauksineen osatunnus)}))
                     skeema/Tutkinnonosa->ArvioinninKohdealueet))))
