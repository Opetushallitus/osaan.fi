;; Copyright (c) 2016 The Finnish National Board of Education - Opetushallitus
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

(ns osaan.rest-api.osaamisala
  (:require [compojure.core :as c]
            [oph.common.util.http-util :refer [json-response]]
            [osaan.compojure-util :as cu]
            [osaan.skeema :as skeema]
            [osaan.arkisto.peruste :as arkisto]))

(c/defroutes reitit
  (cu/defapi :julkinen nil :get "/" [peruste tutkintotunnus]
    (when (arkisto/onko-perustetta (Integer/parseInt peruste))
      (let [alat (arkisto/hae-osaamisalat (Integer/parseInt peruste))]
        (json-response alat [skeema/Osaamisala])))))
