(ns osaan.rest-api.arvioraportti
  (:require [compojure.core :as c]
            [oph.common.util.http-util :refer [json-response]]
            [osaan.arkisto.arvioraportti :as arkisto]
            [osaan.compojure-util :as cu]
            [cheshire.core :as cheshire]))

(defn group-and-destroy [k s]
  (let [grouped (group-by k s)
        cleaned (clojure.walk/postwalk #(if (map? %) (dissoc % k) %) grouped)]
    cleaned))

(defn suodata-fi [tulos]
  (let [suodatettu (map #(select-keys % [ :nimi_fi :osa :aka_nimi_fi :arvio :kommentti]) tulos)
        nimet (map #(clojure.set/rename-keys % {:nimi_fi :nimi
                                                :aka_nimi_fi :arvioinnin_kohdealue
                                                :osa :tutkinnon_osa}) suodatettu )
        groupattu (group-and-destroy :tutkinnon_osa nimet)]
    groupattu))

(def not-found-response
  {:body  "Tarkista tunnus. Arviota ei löytynyt."
   :headers {"Content-Type" "text/plain; charset=utf-8"}
   :status 200})

(c/defroutes reitit
  (cu/defapi :julkinen nil :get "/txt/:kieli/:arviotunnus" [kieli arviotunnus]
    (if-let [tulo (arkisto/hae arviotunnus)]
      (let [tulos (arkisto/tulkitse-arvosanat tulo (keyword kieli))
            suodatettu (suodata-fi tulos)
            muotoiltu (with-out-str (clojure.pprint/pprint suodatettu))]
         {:body muotoiltu
          :headers {"Content-Type" "text/plain; charset=utf-8"}
          :status 200})
      not-found-response))
  (cu/defapi :julkinen nil :get "/json/:arviotunnus" [kieli arviotunnus]
    (if-let [tulos (arkisto/hae arviotunnus)]
        (json-response tulos)
      not-found-response)))