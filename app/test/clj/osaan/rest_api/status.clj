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

(ns osaan.rest-api.status
  (:require [clojure.test :refer :all]
            [osaan.rest-api.session-util :refer :all]
            [osaan.sql.test-util :refer [tietokanta-fixture]]))

(use-fixtures :each tietokanta-fixture)

(deftest ^:integraatio status-responds []
  (let [crout (init-peridot!)]
    (let [response (mock-request! crout "/status" :get {})]
      (is (= (:status (:response response)) 200))
      (is (not= (:body (:response response)) "OK")))))
