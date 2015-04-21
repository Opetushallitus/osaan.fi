(ns osaan.rest-api.session-util
  (:require [peridot.core :as peridot]
            [osaan.asetukset :as osaan-asetukset]
            [osaan.palvelin :as palvelin]))

(defn init-peridot! []
  (let [asetukset
        (-> osaan-asetukset/oletusasetukset
          (assoc :development-mode true))
        crout (palvelin/app asetukset)]
    (peridot/session crout)))

(defn mock-request! [app url method params]
 (peridot/request app url
                  :request-method method
                  :params params))