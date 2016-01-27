(ns osaan.perf-test
  (:require [clojure.test :refer :all]
            [clj-gatling.core :refer [run-simulation]]
            [clojure.tools.logging :as log]
            [osaan.arkisto.peruste :as peruste-arkisto]
            [osaan.arkisto.tutkinnonosa :as tutkinnonosa-arkisto]
            [osaan.asetukset :as asetukset]
            [osaan.perftest-util :as util]
            [osaan.sql.test-util :refer [tietokanta-fixture]]))

(use-fixtures :each tietokanta-fixture)

(def baseurl
  (let [default-base-url (-> (asetukset/hae-asetukset) :server :base-url)
        base-url (if (clojure.string/blank? default-base-url)
                   "http://localhost:8084/"
                   default-base-url)]
    (log/info "base-url on" base-url)
    base-url))

(def basic-auth (or (System/getenv "OSAAN_AUTH") "ei:asetettu"))

(defn generoi-ammattitaidonkuvaus-urleja [lkm]
  (map #(str baseurl "api/ammattitaidonkuvaus/alueet?tutkinnonosatunnus=" %) (take lkm (tutkinnonosa-arkisto/hae-osatunnus-idt-joilla-ammattitaidonkuvaus))))

(defn generoi-tutkinnonosa-urleja [lkm]
  (map #(str baseurl "api/tutkinnonosa?peruste=" %) (take lkm (peruste-arkisto/hae-perusteidt))))

(defn generoi-peruste-urleja [lkm]
  (map #(str baseurl "api/tutkinto/peruste/" %) (take lkm (peruste-arkisto/hae-perusteidt))))

(deftest ^:performance index-testi
  (let [concurrent-users 20
        api-requests [(util/url->http-get-fn basic-auth (str baseurl "api/koulutusala") "/api/koulutusala")
                      (util/url->http-get-fn basic-auth (str baseurl "api/ohje/etusivu") "/api/etusivu")
                      (util/url->http-get-fn basic-auth (str baseurl "api/tutkinto") "/api/tutkinto")]]
    (run-simulation
      [{:name "Staattiset tiedostot"
        :requests [(util/url->http-get-fn basic-auth baseurl "Index")
                   (util/url->http-get-fn basic-auth (str baseurl "js/libs.js") "angular.js")]}
       {:name "API-testit"
        :requests (concat
                    api-requests
                    (for [url (generoi-ammattitaidonkuvaus-urleja 20)]
                      (util/url->http-get-fn basic-auth url "/api/ammattitaidonkuvaus"))
                    (for [url (generoi-tutkinnonosa-urleja 20)]
                      (util/url->http-get-fn basic-auth url "/api/tutkinnonosa"))
                    (for [url (generoi-peruste-urleja 20)]
                      (util/url->http-get-fn basic-auth url "/api/tutkinto/peruste")))}]
      concurrent-users {:root "target/perf-report/perf"
                        :timeout-in-ms 10000
                        :requests 500})))