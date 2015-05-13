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
           [oph.common.util.util :refer :all]
           [osaan.arkisto.peruste :as peruste-arkisto]))

(defn lataa-kaikki-sivut [url options]
  (loop [vanha-data []
         sivu 0]
    (let [{:keys [data sivuja]} (get-json-from-url url (assoc-in options [:query-params :sivu] sivu))
          data (concat vanha-data data)]
      (if (= (inc sivu) sivuja)
        data
        (recur data (inc sivu))))))

(defn muotoile-arvioinnin-kohde [index kohde]
  {:arvioinninkohde_id (get-in kohde [:otsikko :_id])
   :jarjestys (inc index)
   :nimi_fi (get-in kohde [:otsikko :fi])
   :nimi_sv (get-in kohde [:otsikko :sv])})

(defn muotoile-arvioinnin-kohdealue [index alue]
  {:arvioinninkohdealue_id  (get-in alue [:otsikko :_id])
   :jarjestys (inc index)
   :nimi_fi (get-in alue [:otsikko :fi])
   :nimi_sv (get-in alue [:otsikko :sv])
   :arvioinnin_kohteet (map-indexed muotoile-arvioinnin-kohde (:arvioinninKohteet alue))})

(defn osatunnus [osa]
  (cond
    (= 6 (count (:koodiArvo osa))) (:koodiArvo osa)
    (:koodiUri osa) (second (re-matches #"^tutkinnonosat_(\d+)$" (:koodiUri osa)))
    (:koodiArvo osa) (second (re-matches #"^tutkinnonosat_(\d+)$" (:koodiArvo osa)))))

(defn muotoile-tutkinnonosa [index osa]
  {:osatunnus (osatunnus osa)
   :jarjestys (inc index)
   :nimi_fi (get-in osa [:nimi :fi])
   :nimi_sv (get-in osa [:nimi :sv])
   :arvioinnin_kohdealueet (map-indexed muotoile-arvioinnin-kohdealue (get-in osa [:arviointi :arvioinninKohdealueet]))})

(defn hae-osat [rakenne]
  (if (:osat rakenne)
    (mapcat hae-osat (:osat rakenne))
    [rakenne]))

(defn muotoile-suoritustapa
  [osaviite->osatunnus]
  (fn [suoritustapa]
    (let [pakollisuus (into {}
                            (for [osa (hae-osat (:rakenne suoritustapa))]
                              [(:_tutkinnonOsaViite osa) (:pakollinen osa)]))]
      {:suoritustapakoodi (:suoritustapakoodi suoritustapa)
       :osat (map-indexed (fn [index osa]
                            {:jarjestys (inc index)
                             :pakollinen (pakollisuus (str (:id osa)))
                             :tutkinnonosa (osaviite->osatunnus (:_tutkinnonOsa osa))})
                          (:tutkinnonOsaViitteet suoritustapa))})))

(defn muotoile-peruste [peruste]
  (let [osaviite->osatunnus (into {} (for [osa (:tutkinnonOsat peruste)]
                                       [(str (:id osa)) (osatunnus osa)]))]
    {:diaarinumero (:diaarinumero peruste)
     :eperustetunnus (:id peruste)
     :voimassa_alkupvm (c/to-local-date (:voimassaoloAlkaa peruste))
     :voimassa_loppupvm (c/to-local-date (:voimassaoloLoppuu peruste))
     :siirtymaajan_loppupvm (c/to-local-date (:siirtymaPaattyy peruste))
     :tutkinnonosat (map-indexed muotoile-tutkinnonosa (:tutkinnonOsat peruste))
     :tutkinnot (map :koulutuskoodiArvo (:koulutukset peruste))
     :suoritustavat (map (muotoile-suoritustapa osaviite->osatunnus) (:suoritustavat peruste))}))

(defn hae-peruste [id asetukset]
  (muotoile-peruste (get-json-from-url (str (:url asetukset) "api/perusteet/" id "/kaikki"))))

(defn hae-perusteet [viimeisin-haku asetukset]
  (for [peruste (lataa-kaikki-sivut (str (:url asetukset) "api/perusteet") {:query-params {:muokattu (c/to-long viimeisin-haku)}})]
    (hae-peruste (:id peruste) asetukset)))
