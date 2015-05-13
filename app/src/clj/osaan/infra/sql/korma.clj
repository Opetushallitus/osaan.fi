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

(ns osaan.infra.sql.korma
  (:require [korma.core :as sql]
            [oph.korma.common :refer [defentity]]))

(declare tutkinto opintoala koulutusala peruste)

(defentity tutkinto
  (sql/pk :tutkintotunnus)
  (sql/belongs-to opintoala {:fk :opintoala})
  (sql/has-many peruste {:fk :tutkinto}))

(defentity opintoala
  (sql/pk :opintoalatunnus)
  (sql/has-many tutkinto {:fk :opintoala})
  (sql/belongs-to koulutusala {:fk :koulutusala}))

(defentity koulutusala
  (sql/pk :koulutusalatunnus)
  (sql/has-many opintoala {:fk :koulutusala}))

(defentity peruste
  (sql/pk :peruste_id)
  (sql/belongs-to tutkinto {:fk :tutkinto}))

(defentity tutkinnonosa-ja-peruste)

(defentity tutkinnonosa
  (sql/pk :osatunnus))
