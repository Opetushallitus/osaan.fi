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

(ns osaan.arkisto.tutkinnonosa
  (:require [korma.core :as sql]
            [oph.korma.common :as sql-util]))

(defn hae-perusteen-tutkinnon-osat
  "Hae perusteeseen liittyvät tutkinnon osat."
  [peruste-id]
  (sql/select
    :tutkinnonosa
    (sql/join :inner :tutkinnonosa_ja_peruste (= :tutkinnonosa_ja_peruste.osa :tutkinnonosa.osatunnus))
    (sql/fields :tutkinnonosa.nimi_fi
                :tutkinnonosa.nimi_sv
                :tutkinnonosa.osatunnus
                :tutkinnonosa_ja_peruste.tyyppi)
    (sql/where {:tutkinnonosa_ja_peruste.peruste peruste-id})
    (sql/order :tutkinnonosa_ja_peruste.jarjestys)))

(defn hae [osatunnus]
  (sql-util/select-unique-or-nil
    :tutkinnonosa
    (sql/where {:osatunnus osatunnus})))
