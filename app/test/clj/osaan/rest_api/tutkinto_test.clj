(ns osaan.rest-api.tutkinto-test
  (:require [clojure.test :refer :all]
            [osaan.rest-api.session-util :refer :all]
            [osaan.sql.test-util :refer [tietokanta-fixture]]))

(use-fixtures :each tietokanta-fixture)

(deftest ^:integraatio tyhja-opintoala
  (let [crout (init-peridot!)]
    (let [response (mock-request! crout "/api/tutkinto?opintoala=" :get {})]
      (is (= (:status (:response response)) 200))
      (is (< 2 (count (:body (:response response))))))))

