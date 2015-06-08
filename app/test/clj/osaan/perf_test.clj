(ns osaan.perf-test
  (:require
    [clj-gatling.core :refer [run-simulation]])
  (:use clojure.test))

(deftest ^:performance index-testi
  (let [concurrent-users 20
        baseurl "http://localhost:8084/"]
    (run-simulation
      [{:name "Staattiset tiedostot"
        :requests [{:name "Index" :http baseurl}
                   {:name "angular.js" :http (str baseurl "js/libs.js")}]}
       {:name "API-testit"
        :requests [{:name "/api/arvio" :http (str baseurl "api/arvio/testiarvio")}
                   {:name "/api/ammattitaidonkuvaus" :http (str baseurl "api/ammattitaidonkuvaus/alueet?tutkinnonosatunnus=100001&tutkinnonosatunnus=100002")}
                   {:name "/api/koulutusala" :http (str baseurl "api/koulutusala")}
                   {:name "/api/ohje" :http (str baseurl "api/ohje/etusivu")}
                   {:name "/api/tutkinnonosa" :http (str baseurl "api/tutkinnonosa?peruste=-1&tutkintotunnus=324601")}
                   {:name "/api/tutkinto" :http (str baseurl "api/tutkinto")}
                   {:name "/api/tutkinto/peruste" :http (str baseurl "api/tutkinto/peruste/-1")}]}]
      concurrent-users {:root "target/perf-report/perf"
                        :timeout-in-ms 10000
                        :requests 200})))
