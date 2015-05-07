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

(ns osaan.integraatio.eperusteet
 (:require [clojure.tools.logging :as log]
           [clj-time.core :as time]
           [clj-time.coerce :as c]
           [oph.common.util.util :refer :all]))

(defn lataa-kaikki-sivut [url options]
  (loop [vanha-data []
         sivu 0]
    (let [{:keys [data sivuja]} (get-json-from-url url (assoc-in options [:query-params :sivu] sivu))
          data (concat vanha-data data)]
      (if (= (inc sivu) sivuja)
        data
        (recur data (inc sivu))))))

(defn muotoile-arvioinnin-kohde [index alue]
  {:jarjestys (inc index)
   :nimi_fi (get-in alue [:otsikko :fi])
   :nimi_sv (get-in alue [:otsikko :sv])})

(defn muotoile-arvioinnin-kohdealue [index alue]
  {:jarjestys (inc index)
   :nimi_fi (get-in alue [:otsikko :fi])
   :nimi_sv (get-in alue [:otsikko :sv])
   :arvioinnin_kohteet (map-indexed muotoile-arvioinnin-kohde (:arvioinninKohteet alue))})

(defn muotoile-tutkinnonosa [index osa]
  {:osatunnus (:koodiArvo osa)
   :jarjestys (inc index)
   :nimi_fi (get-in osa [:nimi :fi])
   :nimi_sv (get-in osa [:nimi :sv])
   :arvioinnin_kohdealueet (map-indexed muotoile-arvioinnin-kohdealue (get-in osa [:arviointi :arvioinninKohdealueet]))})

(defn muotoile-peruste [peruste]
  {:diaarinumero (:diaarinumero peruste)
   :voimassa_alkupvm (c/from-long (:voimassaoloAlkaa peruste))
   :voimassa_loppupvm (c/from-long (:voimassaoloLoppuu peruste))
   :siirtymaajan_loppupvm (c/from-long (:siirtymaPaattyy peruste))
   :tutkinnonosat (map-indexed muotoile-tutkinnonosa (:tutkinnonOsat peruste))
   :suoritustavat (map :suoritustapakoodi (:suoritustavat peruste))})

(defn hae-peruste [id asetukset]
  (muotoile-peruste (get-json-from-url (str (:url asetukset) "api/perusteet/" id "/kaikki"))))

(defn hae-perusteet [viimeisin-haku asetukset]
  (for [peruste (lataa-kaikki-sivut (str (:url asetukset) "api/perusteet") {:query-params {:muokattu (c/to-long viimeisin-haku)}})
        :let [peruste-data (hae-peruste (:id peruste) asetukset)]
        tutkinto (:koulutukset peruste)
        suoritustapa (:suoritustavat peruste-data)]
    (-> peruste-data
      (assoc :tutkinto (:koulutuskoodiArvo tutkinto)
             :tyyppi suoritustapa)
      (dissoc :suoritustavat))))
