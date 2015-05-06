(ns osaan.rest-api.arvioraportti
  (:require [compojure.core :as c]
            [oph.common.util.http-util :refer [json-response]]
            [osaan.arkisto.arvioraportti :as arkisto]
            [osaan.compojure-util :as cu]
            [cheshire.core :as cheshire]))

(c/defroutes reitit
  (cu/defapi :julkinen nil :get "/txt/:arviotunnus" [arviotunnus]
    (if-let [tulos (arkisto/hae arviotunnus)]
      (let [suodatettu (map #(select-keys % [:nimi_fi :nimi_sv :osa :aka_nimi_fi :aka_nimi_sv :arvio]) tulos)
            muotoiltu (with-out-str (clojure.pprint/pprint suodatettu))]
         {:body muotoiltu
          :headers {"Content-Type" "text/plain; charset=utf-8"}
          :status 200})
      {:body  "Tarkista tunnus. Arviota ei löytynyt."
       :headers {"Content-Type" "text/plain; charset=utf-8"}
       :status 200})))
