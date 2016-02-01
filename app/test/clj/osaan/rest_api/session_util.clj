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

(ns osaan.rest-api.session-util
  (:require [cheshire.core :as cheshire]
            [peridot.core :as peridot]
            [osaan.asetukset :as osaan-asetukset]
            [osaan.palvelin :as palvelin]
            [osaan.sql.test-util :refer [alusta-korma!]]))

(defn init-peridot! []
  (let [asetukset (-> osaan-asetukset/oletusasetukset
                    (assoc :development-mode true))
        crout (palvelin/app asetukset)]
    (peridot/session crout)))

(defn mock-request! [app url method params]
 (peridot/request app url
                  :request-method method
                  :params params))

(defn mock-request-body! [app url method body]
  (peridot/request app url
                   :request-method method
                   :content-type "application/json"
                   :body (cheshire/generate-string body)))
