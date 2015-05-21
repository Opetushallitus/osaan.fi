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
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [compojure.core :as c]
            [compojure.route :as r]
            [ring.util.response :as resp]
            [stencil.core :as s]
            [osaan.infra.status :refer [status piilota-salasanat]]
            [osaan.rest-api.ammattitaidon-kuvaus]
            [osaan.rest-api.arvio]
            [osaan.rest-api.koulutusala]
            [osaan.rest-api.ohje]
            [osaan.rest-api.tutkinnonosa]
            [osaan.rest-api.tutkinto]
            [osaan.rest-api.arvioraportti]))

(defn reitit [asetukset]
  (c/routes
    (c/GET "/" [] (-> (resp/resource-response "index.html" {:root "public/app"})
                      (assoc :headers {"Content-type" "text/html; charset=utf-8"})))
    (if (:development-mode asetukset)
      (c/GET "/status" [] (s/render-file "status" (assoc (status)
                                                         :asetukset (with-out-str
                                                                      (-> asetukset
                                                                        piilota-salasanat
                                                                        pprint)))))
     (c/GET "/status" [] (s/render-string "OK" {})))
    (c/context "/api/ammattitaidonkuvaus" [] osaan.rest-api.ammattitaidon-kuvaus/reitit)
    (c/context "/api/arvio" [] osaan.rest-api.arvio/reitit)
    (c/context "/api/koulutusala" [] osaan.rest-api.koulutusala/reitit)
    (c/context "/api/ohje" [] osaan.rest-api.ohje/reitit)
    (c/context "/api/tutkinnonosa" [] osaan.rest-api.tutkinnonosa/reitit)
    (c/context "/api/tutkinto" [] osaan.rest-api.tutkinto/reitit)
    (c/context "/api/arvioraportti" [] osaan.rest-api.arvioraportti/reitit)
    (r/not-found "Not found")))
