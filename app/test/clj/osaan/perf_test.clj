(ns osaan.perf-test
  (:require
    [clj-gatling.core :refer [run-simulation]]
    [osaan.arkisto.peruste :as peruste-arkisto]
    [osaan.arkisto.tutkinnonosa :as tutkinnonosa-arkisto]
    [osaan.sql.test-util :refer [tietokanta-fixture]])
  (:use clojure.test))

(use-fixtures :each tietokanta-fixture)

(def baseurl "http://localhost:8084/")

(defn generoi-ammattitaidonkuvaus-urleja [lkm]
  (map #(str baseurl "api/ammattitaidonkuvaus/alueet?tutkinnonosatunnus=" %) (take lkm (tutkinnonosa-arkisto/hae-osatunnus-idt-joilla-ammattitaidonkuvaus))))

(defn generoi-tutkinnonosa-urleja [lkm]
  (map #(str baseurl "api/tutkinnonosa?peruste=" %) (take lkm (peruste-arkisto/hae-perusteidt))))

(defn generoi-peruste-urleja [lkm]
  (map #(str baseurl "api/tutkinto/peruste/" %) (take lkm (peruste-arkisto/hae-perusteidt))))

(deftest ^:performance index-testi
  (let [concurrent-users 20
        api-requests [{:name "/api/koulutusala" :http (str baseurl "api/koulutusala")}
                      {:name "/api/ohje" :http (str baseurl "api/ohje/etusivu")}
                      {:name "/api/tutkinto" :http (str baseurl "api/tutkinto")}]]
    (run-simulation
      [{:name "Staattiset tiedostot"
        :requests [{:name "Index" :http baseurl}
                   {:name "angular.js" :http (str baseurl "js/libs.js")}]}
       {:name "API-testit"
        :requests (concat
                    api-requests
                    (for [url (generoi-ammattitaidonkuvaus-urleja 20)]
                      {:name "/api/ammattitaidonkuvaus" :http url})
                    (for [url (generoi-tutkinnonosa-urleja 20)]
                      {:name "/api/tutkinnonosa" :http url})
                    (for [url (generoi-peruste-urleja 20)]
                      {:name "/api/tutkinto/peruste" :http url}))}]
      concurrent-users {:root "target/perf-report/perf"
                        :timeout-in-ms 10000
                        :requests 500})))
