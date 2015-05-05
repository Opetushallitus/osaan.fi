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
  (:require [korma.core :as sql]))

(defn hae-perusteen-tutkinnon-osat
  "Hae perusteeseen liittyvät tutkinnon osat."
  [perusteen-diaarinumero]
  (sql/select
    :peruste
    (sql/join :inner :tutkinnonosa_ja_peruste (= :peruste.diaarinumero :tutkinnonosa_ja_peruste.peruste))
    (sql/join :inner :tutkinnonosa (= :tutkinnonosa_ja_peruste.osa :tutkinnonosa.osatunnus))
    (sql/fields :tutkinnonosa.nimi_fi
                :tutkinnonosa.nimi_sv
                :tutkinnonosa.osatunnus
                :tutkinnonosa_ja_peruste.pakollinen)
    (sql/where {:peruste.diaarinumero perusteen-diaarinumero})))

