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

(ns osaan.arkisto.arvio
  (:require [korma.core :as sql]
            [oph.korma.common :as sql-util]
            [clj-time.coerce :refer [to-sql-time]]
            [osaan.arkisto.peruste :as peruste-arkisto]
            [osaan.arkisto.tutkinnonosa :as tutkinnonosa-arkisto]))

(defn ^:integration-api poista-vanhat-arviot!
  [paivamaara]
  (let [paivamaara (to-sql-time paivamaara)]
    (sql/delete :kohdearvio
      (sql/where {:arviotunnus [in (sql/subselect :arvio
                                     (sql/fields :tunniste)
                                     (sql/where {:luotuaika [< paivamaara]}))]}))
  (sql/delete :arvio_tutkinnonosa
    (sql/where {:arviotunnus [in (sql/subselect :arvio
                                   (sql/fields :tunniste)
                                   (sql/where {:luotuaika [< paivamaara]}))]}))
  (sql/delete :arvio
    (sql/where {:luotuaika [< paivamaara]}))))

(defn ^:private hae-arvio
  [arviotunnus]
  (sql-util/select-unique-or-nil :arvio
    (sql/join :peruste (= :peruste.peruste_id :peruste))
    (sql/fields :peruste [:peruste.tutkinto :tutkintotunnus] :luotuaika)
    (sql/where {:tunniste arviotunnus})))

(defn ^:private hae-kohdearviot
  [arviotunnus]
  (sql/select :kohdearvio
    (sql/join :ammattitaidon_kuvaus (= :ammattitaidon_kuvaus.ammattitaidonkuvaus_id :ammattitaidon_kuvaus))
    (sql/join :arvioinnin_kohdealue (= :arvioinnin_kohdealue.arvioinninkohdealue_id :ammattitaidon_kuvaus.arvioinninkohdealue))
    (sql/fields [:arvioinnin_kohdealue.osa :tutkinnonosa] :ammattitaidon_kuvaus :arvio [:kommentti :vapaateksti])
    (sql/where {:arviotunnus arviotunnus})))

(defn ^:private hae-tutkinnonosat
  [arviotunnus]
  (sql/select :arvio_tutkinnonosa
    (sql/fields :osa :osaamisala)
    (sql/where {:arviotunnus arviotunnus})))

(defn ^:private luo-arviotunniste
  []
  (let [chars (map char (concat (range 48 58) (range 65 91) (range 97 123)))
        tunniste (take 16 (repeatedly #(rand-nth chars)))]
    (reduce str tunniste)))

(defn ^:private tee-tutkinnonosa->osaamisalat [osaamisalat]
  (apply merge-with concat (for [ala osaamisalat
                                 osa (:tutkinnonosat ala)]
                             {(:osatunnus osa) [(:osaamisalatunnus ala)]})))

(defn ^:private muotoile-osaamisalat [osaamisalat tutkinnonosat]
  (if (seq osaamisalat)
    (into {} (for [ala osaamisalat]
               [(:osaamisalatunnus ala) (into {} (for [osa (:tutkinnonosat ala)]
                                                   [(:osatunnus osa) false]))]))
    {nil (into {} (for [osa tutkinnonosat]
                    [(:osatunnus osa) false]))}))

(defn ^:private merkitse-kaikki-osat
  "Merkitsee rakenteesta valituksi kaikki osat annetulla osatunnuksella"
  [alat osatunnus]
  (clojure.walk/postwalk (fn [x]
                           (if (and (vector? x)
                                    (= (first x) osatunnus))
                             [osatunnus true]
                             x))
                         alat))

(defn ^:private taydenna-osaamisalat
  "Lisää arvioon kaikki perusteen osaamisalat ja tutkinnonosat"
  [peruste-id tutkinnonosat]
  (let [osaamisalat (muotoile-osaamisalat (peruste-arkisto/hae-osaamisalat peruste-id)
                                          (tutkinnonosa-arkisto/hae-perusteen-tutkinnon-osat peruste-id))]
    (reduce (fn [alat {:keys [osaamisala osa]}]
              (if osaamisala
                (assoc-in alat [osaamisala osa] true)
                (merkitse-kaikki-osat alat osa)))
            osaamisalat
            tutkinnonosat)))

(defn hae
  [arviotunnus]
  (when-let [arvio (hae-arvio arviotunnus)]
    (let [kohdearviot (hae-kohdearviot arviotunnus)
          tutkinnonosa->kohde->arviot (reduce #(assoc-in %1 [(:tutkinnonosa %2) (:ammattitaidon_kuvaus %2)] (dissoc %2 :tutkinnonosa :ammattitaidon_kuvaus)) {} kohdearviot)
          tutkinnonosat (->>
                          (hae-tutkinnonosat arviotunnus)
                          (taydenna-osaamisalat (:peruste arvio)))]
      (assoc arvio :kohdearviot tutkinnonosa->kohde->arviot
                   :tutkinnonosat tutkinnonosat))))

(defn ^:private numero? [s]
  (try
    (Integer/parseInt s)
    true
    (catch NumberFormatException _
      false)))

(defn ^:private korjaa-numero-avaimet
  "Muuttaa annetusta mapista stringeiksi kaikki keyword-avaimet, jotka koostuvat pelkistä numeroista"
  [m]
  (clojure.walk/postwalk (fn [x]
                           (if (and (keyword? x) (numero? (name x)))
                             (name x)
                             x))
                         m))

(defn tallenna
  [tila]
  (let [{:keys [peruste tutkinnonosat kohdearviot]} (korjaa-numero-avaimet tila)
        tunniste (luo-arviotunniste)]
    (sql/insert :arvio
      (sql/values {:tunniste tunniste
                   :peruste peruste}))
    (sql/insert :arvio_tutkinnonosa
      (sql/values (for [[osaamisalatunnus tutkinnonosat] tutkinnonosat
                        :let [osaamisalatunnus (when-not (= :undefined osaamisalatunnus)
                                                 osaamisalatunnus)]
                        [osatunnus valittu] tutkinnonosat
                        :when valittu]
                    {:arviotunnus tunniste
                     :osa osatunnus
                     :osaamisala osaamisalatunnus})))
    (sql/insert :kohdearvio
      (sql/values (for [[ammattitaidon_kuvaus {:keys [arvio vapaateksti]}] (into {} (vals kohdearviot))]
                    {:arviotunnus tunniste
                     :ammattitaidon_kuvaus (Integer/parseInt (name ammattitaidon_kuvaus))
                     :arvio arvio
                     :kommentti vapaateksti})))
    tunniste))

(defn hae-arviotunnukset []
  (map
    :tunniste
    (sql/select :arvio
      (sql/fields :tunniste))))
