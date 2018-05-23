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

(ns osaan.arkisto.tutkinto
  (:require [korma.core :as sql]
            [oph.korma.common :as sql-util]
            [osaan.infra.sql.korma :as taulut]
            [oph.common.util.util :refer [pvm-mennyt-tai-tanaan? pvm-tuleva-tai-tanaan?]]))

(defn ^:integration-api lisaa!
  [tiedot]
  (sql/insert taulut/tutkinto
    (sql/values tiedot)))

(defn ^:integration-api paivita!
  [tutkintotunnus tiedot]
  (sql-util/update-unique taulut/tutkinto
    (sql/set-fields tiedot)
    (sql/where {:tutkintotunnus tutkintotunnus})))

(defn ^:private hae-yksi [where-ehto]
  (sql-util/select-unique-or-nil
    :tutkinto
    (sql/join :opintoala (= :opintoala.opintoalatunnus :opintoala))
    (sql/join :koulutusala (= :koulutusala.koulutusalatunnus :opintoala.koulutusala))
    (sql/join :peruste (= :peruste.tutkinto :tutkintotunnus))
    (sql/fields :tutkintotunnus :nimi_fi :nimi_sv
                [:koulutusala.nimi_fi :koulutusala_nimi_fi]
                [:koulutusala.nimi_sv :koulutusala_nimi_sv]
                [:opintoala.nimi_fi :opintoala_nimi_fi]
                [:opintoala.nimi_sv :opintoala_nimi_sv]
                :peruste.peruste_id
                [:peruste.diaarinumero :peruste_diaarinumero]
                [:peruste.eperustetunnus :peruste_eperustetunnus]
                [:peruste.tyyppi :peruste_tyyppi])
    (sql/where where-ehto)))

(defn hae-tunnuksella
  "Hae tutkinto tutkintotunnuksella"
  [tunnus]
  (sql-util/select-unique-or-nil :tutkinto
    (sql/where {:tutkintotunnus tunnus})))

(defn hae-perusteella
  "Hae tutkinto tutkinnon perusteen id:llä."
  [id]
  (hae-yksi {:peruste.peruste_id id}))

(defn hae-kaikki
  []
  (sql/select taulut/tutkinto
    (sql/order :tutkintotunnus)))

(defn liita-tutkintonimikkeet [perusteet]
  (let [nimikkeet (sql/select :peruste_ja_tutkintonimike
                    (sql/join :inner :tutkintonimike {:tutkintonimike.nimiketunnus :peruste_ja_tutkintonimike.tutkintonimike})
                    (sql/fields :tutkintonimike.nimi_fi :tutkintonimike.nimi_sv :peruste_ja_tutkintonimike.peruste))
        peruste->nimikkeet (apply merge-with concat (for [nimike nimikkeet]
                                                      {(:peruste nimike) [(dissoc nimike :peruste)]}))]
    (for [peruste perusteet
          :let [nimikkeet (get peruste->nimikkeet (:peruste_id peruste))]]
      (assoc peruste :tutkintonimikkeet nimikkeet))))

(defn hae-ehdoilla
  [nimi kieli opintoala tutkintotaso voimaantulevat]
  (let [nimi (str "%" nimi "%")
        nimi_kentta (get {"fi" :nimi_fi
                          "sv" :nimi_sv} kieli)
        tutkintonimike_nimikentta (get {"fi" :tutkintonimike.nimi_fi
                                        "sv" :tutkintonimike.nimi_sv} kieli)
        osaamisala_nimikentta (get {"fi" :osaamisala.nimi_fi
                                    "sv" :osaamisala.nimi_sv} kieli)
        voimaantulevat (boolean voimaantulevat)
        tutkinnot (->
                    (sql/select* :tutkinto)
                    (sql/join :inner :opintoala (= :opintoala.opintoalatunnus :opintoala))
                    (sql/join :inner :peruste (= :peruste.tutkinto :tutkintotunnus))
                    (sql/fields :tutkintotunnus :nimi_fi :nimi_sv
                                [:opintoala.nimi_fi :opintoala_nimi_fi]
                                [:opintoala.nimi_sv :opintoala_nimi_sv]
                                :peruste.peruste_id
                                [:peruste.diaarinumero :peruste_diaarinumero]
                                [:peruste.eperustetunnus :peruste_eperustetunnus]
                                [:peruste.tyyppi :peruste_tyyppi])
                    (sql/where (sql/sqlfn "not exists" (sql/subselect [:peruste :uusi_peruste]
                                                         (sql/where {:peruste.tutkinto :uusi_peruste.tutkinto
                                                                     :peruste.tyyppi :uusi_peruste.tyyppi})
                                                         (sql/where (and (< :peruste.voimassa_alkupvm :uusi_peruste.voimassa_alkupvm)
                                                                         (or voimaantulevat (< :uusi_peruste.voimassa_alkupvm (sql/raw "current_date"))))))))
                    (sql/where (or {nimi_kentta [sql-util/ilike nimi]}
                                   (sql/sqlfn "exists" (sql/subselect :peruste_ja_tutkintonimike
                                                         (sql/join :inner :tutkintonimike {:peruste_ja_tutkintonimike.tutkintonimike :tutkintonimike.nimiketunnus})
                                                         (sql/where {:peruste_ja_tutkintonimike.peruste :peruste.peruste_id})
                                                         (sql/where {tutkintonimike_nimikentta [sql-util/ilike nimi]})))
                                   (sql/sqlfn "exists" (sql/subselect :osaamisala_ja_peruste
                                                         (sql/join :inner :osaamisala {:osaamisala_ja_peruste.osaamisala :osaamisala.osaamisalatunnus})
                                                         (sql/where {:osaamisala_ja_peruste.peruste :peruste.peruste_id})
                                                         (sql/where {osaamisala_nimikentta [sql-util/ilike nimi]})))))
                    ;; (sql/where (>= :peruste.voimassa_loppupvm (sql/raw "current_date")))
                    (cond->
                      opintoala (sql/where {:opintoala opintoala}))
                    (cond->
                      tutkintotaso (sql/where {:tutkintotaso tutkintotaso}))
                    (cond->
                      (not voimaantulevat) (sql/where (< :peruste.voimassa_alkupvm (sql/raw "current_date"))))
                    sql/exec)]
    (liita-tutkintonimikkeet tutkinnot)))
