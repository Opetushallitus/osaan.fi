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

(ns osaan.rest-api.api-smoke-test
  (:require [clojure.test :refer :all]
            [osaan.rest-api.session-util :refer :all]
            [osaan.sql.test-util :refer [tietokanta-fixture]]))

(use-fixtures :each tietokanta-fixture)

(defn testaa-api
  [polku status-koodi]
  (let [crout (init-peridot!)]
    (let [state (mock-request! crout polku :get {})
          response (:response state)]
      (is (= (:status response) status-koodi) polku)
      (when (= status-koodi 200)
        (is (= (get-in response [:headers "Content-Type"]) "application/json; charset=utf-8") polku)))))

(deftest ^:integraatio api-smoke
  (doseq [polku ["/api/arvio/testiarvio"
                 "/api/ammattitaidonkuvaus/alueet?tutkinnonosatunnus=100001"
                 "/api/ammattitaidonkuvaus/alueet?tutkinnonosatunnus=100001&tutkinnonosatunnus=100002"
                 "/api/koulutusala"
                 "/api/ohje/etusivu"
                 "/api/tutkinnonosa?peruste=-1&tutkintotunnus=324601"
                 "/api/tutkinto"
                 "/api/tutkinto/peruste/-1"]]
    (testaa-api polku 200))
  (doseq [polku ["/api/arvio/puuttuva"
                 "/api/ohje/puuttuva"
                 "/api/tutkinnonosa?peruste=-9999"
                 "/api/tutkinto/peruste/999"]]
    (testaa-api polku 404)))
