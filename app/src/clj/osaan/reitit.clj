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

(ns osaan.reitit
  (:require [clojure.pprint :refer [pprint]]
            [compojure.api.exception :as ex]
            [compojure.api.sweet :refer [GET api context swagger-routes]]
            [compojure.route :as r]
            [ring.util.response :as resp]
            [stencil.core :as s]
            [osaan.asetukset :refer [service-path]]
            [osaan.infra.status :refer [status piilota-salasanat]]
            [osaan.linkki]
            [osaan.rest-api.ammattitaidon-kuvaus]
            [osaan.rest-api.arvio]
            [osaan.rest-api.kaiku]
            [osaan.rest-api.koulutusala]
            [osaan.rest-api.ohje]
            [osaan.rest-api.osaamisala]
            [osaan.rest-api.tutkinnonosa]
            [osaan.rest-api.tutkinto]
            [osaan.rest-api.arvioraportti]))

(defn reitit [asetukset]
  (api
    {:exceptions {:handlers {:schema.core/error ex/schema-error-handler}}}
    (swagger-routes
      {:ui "/api-docs"
       :spec "/swagger.json"
       :data {:info {:title "Osaan.fi API"
                     :description "Osaan.fi-palvelun rajapinnat."}
              :basePath (str (service-path (get-in asetukset [:server :base-url])))}})
    (GET "/" []
      :summary "Etusivu"
      (->
        (resp/resource-response "index.html" {:root "public/app"})
        (assoc :headers {"Content-type" "text/html; charset=utf-8"})))
    (if (:development-mode asetukset)
      (GET "/status" [] (s/render-file "status" (assoc (status)
                                                       :asetukset (with-out-str
                                                                    (-> asetukset
                                                                      piilota-salasanat
                                                                      pprint)))))
     (GET "/status" [] (s/render-string "OK" {})))
    (context "/api/ammattitaidonkuvaus" [] osaan.rest-api.ammattitaidon-kuvaus/reitit)
    (context "/api/arvio" [] osaan.rest-api.arvio/reitit)
    (context "/api/kaiku" [] osaan.rest-api.kaiku/reitit)
    (context "/api/koulutusala" [] osaan.rest-api.koulutusala/reitit)
    (context "/api/ohje" [] osaan.rest-api.ohje/reitit)
    (context "/api/osaamisala" [] osaan.rest-api.osaamisala/reitit)
    (context "/api/tutkinnonosa" [] osaan.rest-api.tutkinnonosa/reitit)
    (context "/api/tutkinto" [] osaan.rest-api.tutkinto/reitit)
    (context "/api/arvioraportti" [] osaan.rest-api.arvioraportti/reitit)
    (context "/linkki" [] (osaan.linkki/reitit asetukset))
    (r/not-found "Not found")))
