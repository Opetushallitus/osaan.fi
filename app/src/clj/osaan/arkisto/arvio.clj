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
            [oph.korma.common :as sql-util]))

(defn ^:private hae-arvio
  [arviotunnus]
  (sql-util/select-unique :arvio
    (sql/join :peruste (= :peruste.diaarinumero :peruste))
    (sql/fields :peruste [:peruste.tutkinto :tutkintotunnus])
    (sql/where {:tunniste arviotunnus})))

(defn ^:private hae-kohdearviot
  [arviotunnus]
  (sql/select :kohdearvio
    (sql/fields :arvioinnin_kohde :arvio :kommentti)
    (sql/where {:arviotunnus arviotunnus})))

(defn ^:private hae-tutkinnonosat
  [arviotunnus]
  (sql/select :arvio_tutkinnonosa
    (sql/fields :osa)
    (sql/where {:arviotunnus arviotunnus})))

(defn hae
  [arviotunnus]
  (let [arvio (hae-arvio arviotunnus)
        kohdearviot (hae-kohdearviot arviotunnus)
        tutkinnonosat (hae-tutkinnonosat arviotunnus)]
    (assoc arvio :kohdearviot kohdearviot
                 :tutkinnonosat (map :osa tutkinnonosat))))
