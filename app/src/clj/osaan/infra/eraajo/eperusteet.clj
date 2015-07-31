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

(ns osaan.infra.eraajo.eperusteet
  (:require [clj-time.core :as time]
            [clojurewerkz.quartzite.conversion :as qc]
            [clojure.tools.logging :as log]
            [korma.db :as db]
            [osaan.arkisto.peruste :as peruste-arkisto]
            [osaan.integraatio.eperusteet :as eperusteet]
            [osaan.infra.kayttaja.sql :refer [with-sql-kayttaja integraatio-kayttaja]]))

(defn paivita-tutkintojen-perusteet [asetukset]
  (with-sql-kayttaja integraatio-kayttaja
    (db/transaction
      (log/info "Päivitetään tutkintojen perusteet ePerusteet-järjestelmästä")
      (let [nyt (time/now)
            viimeisin-paivitys (when-not (:hae-kaikki asetukset)
                                 (peruste-arkisto/hae-viimeisin-paivitys))
            perusteet (eperusteet/hae-perusteet viimeisin-paivitys asetukset)]
        (log/info "Perusteet haettu," (count perusteet) "kpl päivittyneitä perusteita")
        (doseq [peruste perusteet]
          (log/info "Päivitetään peruste" (:diaarinumero peruste))
          (peruste-arkisto/lisaa! peruste))
        (peruste-arkisto/tallenna-viimeisin-paivitys! nyt))
      (log/info "Tutkintojen perusteiden päivitys valmis."))))

;; Cloverage ei tykkää `defrecord`eja generoivista makroista, joten hoidetaan
;; `defjob`:n homma käsin.
(defrecord PaivitaPerusteetJob []
   org.quartz.Job
   (execute [this ctx]
     (let [{asetukset "asetukset"} (qc/from-job-data ctx)]
       (paivita-tutkintojen-perusteet (clojure.walk/keywordize-keys asetukset)))))
