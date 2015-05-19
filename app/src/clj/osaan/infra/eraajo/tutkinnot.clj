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

(ns osaan.infra.eraajo.tutkinnot
  (:require [clojurewerkz.quartzite.conversion :as qc]
            [clojure.tools.logging :as log]
            [osaan.integraatio.koodistopalvelu :as org]
            [osaan.infra.kayttaja.sql :refer [with-sql-kayttaja integraatio-kayttaja]]))

(defn ^:integration-api paivita-tutkinnot! [asetukset]
  (with-sql-kayttaja integraatio-kayttaja
    (org/paivita-tutkinnot! asetukset)))

;; Cloverage ei tykkää `defrecord`eja generoivista makroista, joten hoidetaan
;; `defjob`:n homma käsin.
(defrecord PaivitaTutkinnotJob []
   org.quartz.Job
   (execute [this ctx]
     (let [{asetukset "asetukset"} (qc/from-job-data ctx)]
       (paivita-tutkinnot! (clojure.walk/keywordize-keys asetukset)))))
