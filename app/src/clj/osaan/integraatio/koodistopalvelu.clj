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

(ns osaan.integraatio.koodistopalvelu
 (:require [clj-time.core :as time]
           [clojure.set :refer [intersection difference rename-keys]]
           [oph.common.util.util :refer :all]
           [osaan.arkisto.tutkinto :as tutkinto-arkisto]
           [osaan.arkisto.opintoala :as opintoala-arkisto]
           [osaan.arkisto.koulutusala :as koulutusala-arkisto]
           [clojure.tools.logging :as log]
           [korma.db :as db]))

;; Tässä nimiavaruudessa viitataan "koodi"-sanalla koodistopalvelun palauttamaan tietorakenteeseen.
;; Jos koodi on muutettu Osaan.fi:n käyttämään muotoon, siihen viitataan ko. käsitteen nimellä, esim. "osatutkinto".


;  "koulutusalaoph2002"
(def ^:private koulutusala-koodisto "isced2011koulutusalataso1")

; "opintoalaoph2002"
(def ^:private opintoala-koodisto "isced2011koulutusalataso2")

(defn koodi->kasite
  "Muuttaa koodistopalvelun koodin ohjelmassa käytettyyn muotoon.
Koodin arvo laitetaan arvokentta-avaimen alle."
  [koodi arvokentta]
  (when koodi
    (let [metadata_fi (first (filter #(= "FI" (:kieli %)) (:metadata koodi)))
          metadata_sv (first (filter #(= "SV" (:kieli %)) (:metadata koodi)))]
      {:nimi_fi (:nimi metadata_fi)
       :nimi_sv (:nimi metadata_sv)
       :voimassa_alkupvm (some-> (:voimassaAlkuPvm koodi) parse-ymd)
       :voimassa_loppupvm (or (some-> (:voimassaLoppuPvm koodi) parse-ymd) (time/local-date 2199 1 1))
       :koodiUri (:koodiUri koodi)
       arvokentta (:koodiArvo koodi)})))

(defn koodi->tutkinto [koodi]
  (koodi->kasite koodi :tutkintotunnus))

(defn koodi->koulutusala [koodi]
  (koodi->kasite koodi :koulutusalatunnus))

(defn koodi->opintoala [koodi]
  (koodi->kasite koodi :opintoalatunnus))

(defn ^:private hae-koodit
  "Hakee kaikki koodit annetusta koodistosta ja asettaa koodin koodiarvon avaimeksi arvokentta"
  ([asetukset koodisto] (get-json-from-url (str (:url asetukset) koodisto "/koodi") {:headers {"Caller-Id" (:caller-id asetukset)}})))

(defn kuuluu-koodistoon
  "Filtteröi koodilistasta annetun koodiston koodit"
  [koodisto]
  (fn [koodi]
    (= koodisto (get-in koodi [:koodisto :koodistoUri]))))

(defn ^:private opintoala-koodi?
  [koodi]
  ((kuuluu-koodistoon opintoala-koodisto) koodi))

(defn ^:private koulutusala-koodi?
  [koodi]
  ((kuuluu-koodistoon koulutusala-koodisto) koodi))

(defn ^:private tutkintotyyppi-koodi?
  [koodi]
  ((kuuluu-koodistoon "tutkintotyyppi") koodi))

(defn ^:private tutkintotaso-koodi?
  [koodi]
  ((kuuluu-koodistoon "koulutustyyppi") koodi))

(defn ^:private koodi->tutkintotaso
  [koodi]
  (when (tutkintotaso-koodi? koodi)
    (case (:koodiArvo koodi)
      "1" "perustutkinto"
      "4" "perustutkinto"
      "11" "ammattitutkinto"
      "12" "erikoisammattitutkinto"
      "13" "perustutkinto"
      nil)))

(defn ^:private lisaa-opintoalaan-koulutusala
  [asetukset opintoala]
  (let [ylakoodit (get-json-from-url (str (:url asetukset) "relaatio/sisaltyy-ylakoodit/" (:koodiUri opintoala)) {:headers {"Caller-Id" (:caller-id asetukset)}})
        koulutusala (some-value koulutusala-koodi? ylakoodit)]
    (assoc opintoala :koulutusala (:koodiArvo koulutusala))))

(defn ^:private hae-alakoodit
  [asetukset koodi] (get-json-from-url (str (:url asetukset) "relaatio/sisaltyy-alakoodit/" (:koodiUri koodi)) {:headers {"Caller-Id" (:caller-id asetukset)}}))

(defn lisaa-opintoala-ja-tutkintotyyppi
  [asetukset tutkinto]
  (let [alakoodit (hae-alakoodit asetukset tutkinto)
        opintoala (some-value opintoala-koodi? alakoodit)
        tyyppi (some-value tutkintotyyppi-koodi? alakoodit)
        taso (some koodi->tutkintotaso alakoodit)]
    (assoc tutkinto
           :opintoala (:koodiArvo opintoala)
           :tyyppi (:koodiArvo tyyppi)
           :tutkintotaso taso)))

(defn hae-koodisto
  [asetukset koodisto]
  (koodi->kasite (get-json-from-url (str (:url asetukset) koodisto) {:headers {"Caller-Id" (:caller-id asetukset)}}) :koodisto))

(defn hae-tutkinnot
  [asetukset]
  (map koodi->tutkinto (hae-koodit asetukset "koulutus")))

(defn hae-koulutusalat
  [asetukset]
  (->> (hae-koodit asetukset koulutusala-koodisto)
    (map koodi->koulutusala)
    (map #(dissoc % :kuvaus_fi :kuvaus_sv))))

(defn hae-opintoalat
  [asetukset]
  (->> (hae-koodit asetukset opintoala-koodisto)
    (map koodi->opintoala)
    (map (partial lisaa-opintoalaan-koulutusala asetukset))
    (map #(dissoc % :kuvaus_fi :kuvaus_sv))))

(defn muutokset
  [uusi vanha]
  (into {}
        (for [[avain [uusi-arvo vanha-arvo :as diff]] (diff-maps uusi vanha)
              :when diff]
          [avain (cond
                   (nil? uusi-arvo) diff
                   (nil? vanha-arvo) diff
                   (map? uusi-arvo) (diff-maps uusi-arvo vanha-arvo)
                   :else diff)])))

(defn hae-tutkinto-muutokset
  [asetukset]
  (let [tutkinto-kentat [:nimi_fi :nimi_sv :voimassa_alkupvm :voimassa_loppupvm :tutkintotunnus :opintoala :tutkintotaso]
        vanhat (into {} (for [tutkinto (tutkinto-arkisto/hae-kaikki)]
                          [(:tutkintotunnus tutkinto) (select-keys tutkinto tutkinto-kentat)]))
        uudet (->> (hae-tutkinnot asetukset)
                (map (partial lisaa-opintoala-ja-tutkintotyyppi asetukset))
                (filter #(not (nil? (:tutkintotaso %1))))
                (map #(select-keys % tutkinto-kentat))
                (map-by :tutkintotunnus))]
    (muutokset uudet vanhat)))

(defn hae-koulutusala-muutokset
  [asetukset]
  (let [koulutusala-kentat [:nimi_fi :nimi_sv :voimassa_alkupvm :voimassa_loppupvm :koulutusalatunnus]
        vanhat (into {} (for [koulutusala (koulutusala-arkisto/hae-kaikki)]
                          [(:koulutusalatunnus koulutusala) (select-keys koulutusala koulutusala-kentat)]))
        uudet (map-by :koulutusalatunnus
                      (map #(select-keys % koulutusala-kentat) (hae-koulutusalat asetukset)))]
    (muutokset uudet vanhat)))

(defn hae-opintoala-muutokset
  [asetukset]
  (let [opintoala-kentat [:nimi_fi :nimi_sv :voimassa_alkupvm :voimassa_loppupvm :opintoalatunnus :koulutusala]
        vanhat (into {} (for [opintoala (opintoala-arkisto/hae-kaikki)]
                          [(:opintoalatunnus opintoala) (select-keys opintoala opintoala-kentat)]))
        uudet (map-by :opintoalatunnus
                      (map #(select-keys % opintoala-kentat) (hae-opintoalat asetukset)))]
    (muutokset uudet vanhat)))

(defn uusi
  "Jos muutos on uuden tiedon lisääminen, palauttaa uudet tiedot, muuten nil"
  [[tutkintotunnus muutos]]
  (when (vector? muutos)
    (assoc (first muutos) :tutkintotunnus tutkintotunnus)))

(defn uudet-arvot [muutos]
  (into {}
        (for [[k v] muutos
             :when v]
         [k (first v)])))

(defn muuttunut
  "Jos muutos on tietojen muuttuminen, palauttaa muuttuneet tiedot, muuten nil"
  [[tutkintotunnus muutos]]
  (when (and (map? muutos)
             (not-every? nil? (vals muutos)))
    (merge {:tutkintotunnus tutkintotunnus}
          (uudet-arvot muutos))))

(defn ^:integration-api tallenna-uudet-koulutusalat! [koulutusalat]
  (doseq [ala koulutusalat]
    (log/info "Lisätään koulutusala " (:koulutusalatunnus ala))
    (koulutusala-arkisto/lisaa! ala)))

(defn ^:integration-api tallenna-muuttuneet-koulutusalat! [koulutusalat]
  (doseq [ala koulutusalat
          :let [tunnus (:koulutusalatunnus ala)
                ala (dissoc ala :koulutusalatunnus)]]
    (log/info "Päivitetään koulutusala " tunnus ", muutokset: " ala)
    (koulutusala-arkisto/paivita! tunnus ala)))

(defn ^:integration-api tallenna-koulutusalat! [koulutusalat]
  (let [uudet (for [[alakoodi ala] koulutusalat
                    :when (and (vector? ala) (first ala))]
                (first ala))
        muuttuneet (for [[alakoodi ala] koulutusalat
                         :when (map? ala)]
                     (assoc (uudet-arvot ala) :koulutusalatunnus alakoodi))]
    (tallenna-uudet-koulutusalat! uudet)
    (tallenna-muuttuneet-koulutusalat! muuttuneet)))

(defn ^:integration-api tallenna-uudet-opintoalat! [opintoalat]
  (doseq [ala opintoalat]
    (log/info "Lisätään opintoala " (:opintoalatunnus ala))
    (opintoala-arkisto/lisaa! ala)))

(defn ^:integration-api tallenna-muuttuneet-opintoalat! [opintoalat]
  (doseq [ala opintoalat
          :let [tunnus (:opintoalatunnus ala)
                ala (dissoc ala :opintoalatunnus)]]
    (log/info "Päivitetään opintoala " tunnus ", muutokset: " ala)
    (opintoala-arkisto/paivita! tunnus ala)))

(defn ^:integration-api tallenna-opintoalat! [opintoalat]
  (let [uudet (for [[alakoodi ala] opintoalat
                    :when (and (vector? ala) (first ala))]
                (first ala))
        muuttuneet (for [[alakoodi ala] opintoalat
                         :when (map? ala)]
                     (assoc (uudet-arvot ala) :opintoalatunnus alakoodi))]
    (tallenna-uudet-opintoalat! (filter :koulutusala uudet))
    (tallenna-muuttuneet-opintoalat! (filter :koulutusala muuttuneet))))

(defn ^:integration-api tallenna-uudet-tutkinnot! [tutkinnot]
  (doseq [tutkinto tutkinnot]
    (log/info "Lisätään tutkinto " (:tutkintotunnus tutkinto))
    (tutkinto-arkisto/lisaa! tutkinto)))

(defn ^:integration-api tallenna-muuttuneet-tutkinnot! [tutkinnot]
  (doseq [tutkinto tutkinnot
          :let [tunnus (:tutkintotunnus tutkinto)
                tutkinto (dissoc tutkinto :tutkintotunnus)]]
    (if (nil? (:opintoala tutkinto))
      (log/info "Tutkinto " tunnus ", opintoalaa ei löytynyt. Ei päivitetty.")
      (do
        (log/info "Päivitetään tutkinto " tunnus ", muutokset: " tutkinto)
        (tutkinto-arkisto/paivita! tunnus tutkinto)))))

(defn ^:integration-api tallenna-tutkinnot! [tutkinnot]
  (let [uudet (for [[tunnus tutkinto] tutkinnot
                    :when (and (vector? tutkinto) (first tutkinto))]
                (first tutkinto))
        muuttuneet (for [[tunnus tutkinto] tutkinnot
                         :when (map? tutkinto)]
                     (assoc (uudet-arvot tutkinto) :tutkintotunnus tunnus))]
    (tallenna-uudet-tutkinnot! (filter :opintoala uudet))
    (tallenna-muuttuneet-tutkinnot! muuttuneet)))

(defn ^:integration-api paivita-tutkinnot! [asetukset]
  (try
    (db/transaction
      (log/info "Aloitetaan tutkintojen päivitys koodistopalvelusta")
      (tallenna-koulutusalat! (hae-koulutusala-muutokset asetukset))
      (tallenna-opintoalat! (hae-opintoala-muutokset asetukset))
      (tallenna-tutkinnot! (hae-tutkinto-muutokset asetukset))
      (log/info "Tutkintojen päivitys koodistopalvelusta valmis"))
    (catch org.postgresql.util.PSQLException e
      (log/error e "Tutkintojen päivitys koodistopalvelusta epäonnistui"))))

