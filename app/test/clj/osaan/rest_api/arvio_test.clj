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

(ns osaan.rest-api.arvio_test
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [osaan.rest-api.session-util :refer :all]
            [osaan.sql.test-util :refer [tietokanta-fixture]]))

(use-fixtures :each tietokanta-fixture)

(def arvio {:kohdearviot {"100001" {"-1" {:arvio 3, :vapaateksti "testivastaus"}}},
            :tutkinnonosat ["100001" "100002" "100003"],
            :peruste -1,
            :tutkintotunnus "324601"})

(deftest ^:integraatio tallennus-ja-lataus []
  (let [crout (init-peridot!)
        state-tallennus (mock-request-body! crout "/api/arvio" :post arvio)]
    (is (= (:status (:response state-tallennus)) 200))
    (let [tunnus (-> state-tallennus :response :body cheshire/parse-string)
          state-lataus (mock-request! crout (str "/api/arvio/" tunnus) :get {})
          ladattu-arvio (-> state-lataus :response :body cheshire/parse-string)]
      (is (= (cheshire/parse-string (cheshire/generate-string arvio)) ladattu-arvio)))))
