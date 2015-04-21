(ns osaan.rest-api.status
  (:require [osaan.rest-api.session-util :refer :all]
            [clojure.test :refer :all]))

(deftest ^:integraatio status-responds []
  (let [crout (init-peridot!)]
    (let [response (mock-request! crout "/status" :get {})]
      (is (= (:status (:response response)) 200))
      (is (= (:body (:response response)) "OK")))))
