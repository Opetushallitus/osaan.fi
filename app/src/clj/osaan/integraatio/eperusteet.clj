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
      (if (>= (inc sivu) sivuja)
        data
        (recur data (inc sivu))))))

(defn numeroi [index m]
  (assoc m :jarjestys (inc index)))

(defn osaamistaso-hyva [tasot]
  (some-value-with :_osaamistaso "3" tasot))

(defn muotoile-ammattitaidon-kuvaukset [kohde]
  (for [kriteeri (or (:kriteerit (osaamistaso-hyva (:osaamistasonKriteerit kohde)))
                     (:kriteerit (first (:osaamistasonKriteerit kohde))))] ;; Vain perustutkinnoilla on erilliset osaamistasot määriteltyinä
    {:nimi_fi (:fi kriteeri)
     :nimi_sv (:sv kriteeri)}))

(defn muotoile-osa-alue [index alue]
  {:jarjestys (inc index)
   :nimi_fi (get-in alue [:nimi :fi])
   :nimi_sv (get-in alue [:nimi :sv])
   :ammattitaidon_kuvaukset (map-indexed numeroi (mapcat muotoile-ammattitaidon-kuvaukset (-> alue :arviointi :arvioinninKohdealueet first :arvioinninKohteet)))})

(defn muotoile-arvioinnin-kohdealue [index alue]
  {:jarjestys (inc index)
   :nimi_fi (get-in alue [:otsikko :fi])
   :nimi_sv (get-in alue [:otsikko :sv])
   :ammattitaidon_kuvaukset (map-indexed numeroi (mapcat muotoile-ammattitaidon-kuvaukset (:arvioinninKohteet alue)))})

(defn osatunnus [osa]
  (cond
    (= 6 (count (:koodiArvo osa))) (:koodiArvo osa)
    (:koodiUri osa) (second (re-matches #"^tutkinnonosat_(\d+)$" (:koodiUri osa)))
    (:koodiArvo osa) (second (re-matches #"^tutkinnonosat_(\d+)$" (:koodiArvo osa)))))

(defn muotoile-tutkinnonosa [index osa]
  (let [kohdealueet (map-indexed muotoile-arvioinnin-kohdealue (get-in osa [:arviointi :arvioinninKohdealueet]))
        osa-alueet (map-indexed muotoile-osa-alue (mapcat :osaamistavoitteet (:osaAlueet osa)))]
    {:osatunnus (osatunnus osa)
     :jarjestys (inc index)
     :nimi_fi (get-in osa [:nimi :fi])
     :nimi_sv (get-in osa [:nimi :sv])
     :arvioinnin_kohdealueet (if (seq kohdealueet)
                               kohdealueet
                               osa-alueet)}))

(defn yhteinen-osa? [rakenne]
  (= (get-in rakenne [:nimi :fi]) "Yhteiset tutkinnon osat"))

(defn osaamisalatunnus [rakenne]
  (second (re-matches #"^osaamisala_(.*)$" (get-in rakenne [:osaamisala :osaamisalakoodiUri] ""))))

(defn hae-osat
  ([rakenne] (hae-osat "valinnainen" nil rakenne))
  ([tyyppi osaamisala rakenne]
    (let [osaamisala (or osaamisala (osaamisalatunnus rakenne))
          tyyppi (cond
                   (yhteinen-osa? rakenne) "yhteinen"
                   (:pakollinen rakenne) "pakollinen"
                   :else tyyppi)]
      (if (:osat rakenne)
       (mapcat (partial hae-osat tyyppi osaamisala) (:osat rakenne))
       [(assoc rakenne :tyyppi tyyppi, :osaamisala osaamisala)]))))

(defn muotoile-osaamisala
  [ala]
  {:osaamisalatunnus (second (re-matches #"^osaamisala_(.*)$" (:uri ala)))
   :nimi_fi (get-in ala [:nimi :fi])
   :nimi_sv (get-in ala [:nimi :sv])})

(defn muotoile-suoritustapa
  [osa-id->osatunnus osaamisalat]
  (fn [suoritustapa]
    (let [osaviite->osatunnus (into {} (for [viite (:tutkinnonOsaViitteet suoritustapa)]
                                        [(str (:id viite)) (osa-id->osatunnus (:_tutkinnonOsa viite))]))
          tutkinnonosat (map-indexed (fn [index osa]
                                       {:jarjestys (inc index)
                                        :tyyppi (:tyyppi osa)
                                        :tutkinnonosa (osaviite->osatunnus (:_tutkinnonOsaViite osa))
                                        :osaamisala (:osaamisala osa)})
                          (hae-osat (:rakenne suoritustapa)))
          osaamisalan-tutkinnonosat (group-by :osaamisala tutkinnonosat)
          osaamisalat (for [ala osaamisalat
                            :let [osat (concat (osaamisalan-tutkinnonosat nil)
                                               (osaamisalan-tutkinnonosat (:osaamisalatunnus ala)))]]
                        (assoc ala :osat osat))]
      (doseq [ala osaamisalat]
        (when-not (seq (osaamisalan-tutkinnonosat (:osaamisalatunnus ala)))
          (log/warn "Osaamisalalla ei ole tutkinnonosia:" (:osaamisalatunnus ala) (:nimi_fi ala))))
      {:suoritustapakoodi (:suoritustapakoodi suoritustapa)
       :osat tutkinnonosat
       :osaamisalat osaamisalat})))

(defn nimike-kielella [nimike kieli]
  (let [kieli (case kieli
                :fi "FI"
                :sv "SV")
        nimiketunnus (:tutkintonimikeArvo nimike)]
    (:nimi (some-value-with :kieli kieli (get-in nimike [:b (keyword nimiketunnus) :metadata])))))

(defn muotoile-peruste [peruste nimikkeet]
  (let [osa-id->osatunnus (into {} (for [osa (:tutkinnonOsat peruste)]
                                     [(str (:id osa)) (osatunnus osa)]))
        nimiketunnus->nimike (into {} (for [nimike nimikkeet
                                            :let [nimiketunnus (:tutkintonimikeArvo nimike)]]
                                        [nimiketunnus {:nimiketunnus nimiketunnus
                                                       :nimi_fi (nimike-kielella nimike :fi)
                                                       :nimi_sv (nimike-kielella nimike :sv)}]))
        osaamisalat (map muotoile-osaamisala (:osaamisalat peruste))]
    {:diaarinumero (:diaarinumero peruste)
     :eperustetunnus (:id peruste)
     :voimassa_alkupvm (c/to-local-date (:voimassaoloAlkaa peruste))
     :voimassa_loppupvm (c/to-local-date (:voimassaoloLoppuu peruste))
     :siirtymaajan_loppupvm (c/to-local-date (:siirtymaPaattyy peruste))
     :tutkinnonosat (map-indexed muotoile-tutkinnonosa (:tutkinnonOsat peruste))
     :tutkinnot (map :koulutuskoodiArvo (:koulutukset peruste))
     :tutkintonimikkeet (map (comp nimiketunnus->nimike :tutkintonimikeArvo) (:tutkintonimikkeet peruste))
     :suoritustavat (map (muotoile-suoritustapa osa-id->osatunnus osaamisalat) (:suoritustavat peruste))}))

(defn hae-peruste [id asetukset]
  (let [peruste-data (get-json-from-url (str (:url asetukset) "api/perusteet/" id "/kaikki") {:headers {"Caller-Id" (:caller-id asetukset)}})
        nimike-data (get-json-from-url (str (:url asetukset) "api/perusteet/" id "/tutkintonimikekoodit") {:headers {"Caller-Id"(:caller-id asetukset)}})]
    (muotoile-peruste peruste-data nimike-data)))

(defn hae-perusteet [viimeisin-haku asetukset]
  (let [perusteet (lataa-kaikki-sivut (str (:url asetukset) "api/perusteet") {:query-params {:muokattu (c/to-long viimeisin-haku)}
                                                                              :headers {"Caller-Id" (:caller-id asetukset)}})]
    (for [peruste perusteet]
      (do
        (log/info "Haetaan perustetta" (:diaarinumero peruste))
        (hae-peruste (:id peruste) asetukset)))))
