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

(ns osaan.arkisto.peruste
  (:require [clj-time.core :as time]
            [korma.core :as sql]
            [oph.common.util.util :refer [update-in-if-exists]]
            [oph.korma.common :as sql-util]
            [osaan.arkisto.tutkinnonosa :as tutkinnonosa-arkisto]
            [osaan.arkisto.tutkinto :as tutkinto-arkisto]
            [osaan.infra.sql.korma :as taulut]
            [clojure.tools.logging :as log]))

(defn taydenna-nimi [data virheilmoitus]
  (cond
    (and (nil? (:nimi_fi data))
         (:nimi_sv data))        (do
                                   (log/warn virheilmoitus data)
                                   (assoc data :nimi_fi (:nimi_sv data)))
    (and (nil? (:nimi_fi data))
         (nil? (:nimi_sv data))) (log/warn virheilmoitus data)
    :else data))

(defn ^:integration-api paivita-ammattitaidon-kuvaus! [kuvaus]
  (when-let [kuvaus (taydenna-nimi kuvaus "Puutteellinen ammattitaidon kuvaus")]
    (sql-util/insert-or-update :ammattitaidon_kuvaus [:arvioinninkohdealue :jarjestys]
                               (select-keys kuvaus [:nimi_fi :nimi_sv :arvioinninkohdealue :jarjestys]))))

(defn ^:integration-api paivita-arvioinnin-kohdealue! [alue]
  (when-let [tallennettava-alue (taydenna-nimi (select-keys alue [:nimi_fi :nimi_sv :osa :jarjestys]) "Puutteellinen arvioinnin kohdealue")]
    (if (some nil? (map tallennettava-alue [:osa :jarjestys :nimi_fi]))
      (log/warn "Puutteellinen arvioinnin kohdealue:" tallennettava-alue)
      (let [tallennettu-alue (sql-util/insert-or-update :arvioinnin_kohdealue [:osa :jarjestys]
                                                        (select-keys tallennettava-alue [:nimi_fi :nimi_sv :osa :jarjestys]))]
        (doseq [kuvaus (:ammattitaidon_kuvaukset alue)]
          (paivita-ammattitaidon-kuvaus! (assoc kuvaus :arvioinninkohdealue (:arvioinninkohdealue_id tallennettu-alue))))))))

(defn ^:integration-api paivita-tutkinnonosa! [osa]
  (when-let [tallennettava-osa (taydenna-nimi (select-keys osa [:osatunnus :nimi_fi :nimi_sv]) "Puutteellinen tutkinnonosa")]
    (if (some nil? (map tallennettava-osa [:osatunnus :nimi_fi]))
      (log/warn "Puutteellinen tutkinnonosa:" tallennettava-osa)
      (do
        (sql-util/insert-or-update :tutkinnonosa :osatunnus
                                   (select-keys osa [:osatunnus :nimi_fi :nimi_sv]))
        (doseq [alue (:arvioinnin_kohdealueet osa)]
          (paivita-arvioinnin-kohdealue! (assoc alue :osa (:osatunnus osa))))))))

(defn ^:integration-api paivita-perusteen-tutkinnonosat! [peruste osat]
  (doseq [{:keys [tutkinnonosa tyyppi jarjestys]} osat]
    (if (tutkinnonosa-arkisto/hae tutkinnonosa)
      (sql-util/insert-or-update :tutkinnonosa_ja_peruste [:osa :peruste]
        {:osa tutkinnonosa
         :peruste (:peruste_id peruste)
         :jarjestys jarjestys
         :tyyppi tyyppi})
      (log/warn "Tutkinnonosa puuttuu:" tutkinnonosa))))

(defn ^:integration-api paivita-perusteen-tutkintonimikkeet! [peruste nimikkeet]
  (sql/delete :peruste_ja_tutkintonimike
    (sql/where {:peruste (:peruste_id peruste)}))
  (doseq [nimike nimikkeet]
    (sql-util/insert-or-update :tutkintonimike [:nimiketunnus] nimike)
    (sql-util/insert-if-not-exists :peruste_ja_tutkintonimike {:peruste (:peruste_id peruste)
                                                               :tutkintonimike (:nimiketunnus nimike)})))

(defn ^:integration-api lisaa! [peruste]
  (doseq [osa (:tutkinnonosat peruste)]
    (paivita-tutkinnonosa! osa))
  (doseq [tapa (:suoritustavat peruste)
          tutkinto (:tutkinnot peruste)
          :let [tallennettu-peruste (sql-util/insert-or-update taulut/peruste [:diaarinumero :tyyppi :tutkinto]
                                      (assoc (select-keys peruste [:diaarinumero :eperustetunnus :nimi_fi :nimi_sv :voimassa_alkupvm])
                                             :tutkinto tutkinto
                                             :tyyppi (:suoritustapakoodi tapa)
                                             :voimassa_loppupvm (or (:voimassa_loppupvm peruste) (time/local-date 2199 1 1))
                                             :siirtymaajan_loppupvm (or (:siirtymaajan_loppupvm peruste) (time/local-date 2199 1 1))))]]
    (paivita-perusteen-tutkinnonosat! tallennettu-peruste (:osat tapa))
    (paivita-perusteen-tutkintonimikkeet! tallennettu-peruste (:tutkintonimikkeet peruste))))

(defn ^:integration-api tallenna-viimeisin-paivitys! [ajankohta]
  (sql/insert taulut/eperusteet-log
    (sql/values {:paivitetty ajankohta})))

(defn hae-viimeisin-paivitys []
  (:paivitetty (sql-util/select-unique-or-nil taulut/eperusteet-log
                 (sql/order :id :desc)
                 (sql/limit 1)
                 (sql/fields :paivitetty))))

(defn hae-perusteidt []
  (map
    :peruste_id
    (sql/select :peruste
      (sql/fields :peruste_id))))

(defn onko-perustetta [peruste-id]
  (not
    (empty?
      (sql-util/select-unique-or-nil
        :peruste
        (sql/fields :peruste_id)
        (sql/where {:peruste_id peruste-id})))))
