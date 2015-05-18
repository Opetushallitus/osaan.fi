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

(ns osaan.infra.eraajo
  "Säännöllisin väliajoin suoritettavat toiminnot."
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.schedule.daily-interval :as s]
            [clojurewerkz.quartzite.schedule.cron :as cron]
            [clojure.tools.logging :as log]
            osaan.infra.eraajo.eperusteet)
  (:import osaan.infra.eraajo.eperusteet.PaivitaPerusteetJob))

(defn ajastus [asetukset tyyppi]
  (cron/schedule
    (cron/cron-schedule (get-in asetukset [:ajastus tyyppi]))))

(defn ^:integration-api kaynnista-ajastimet! [asetukset]
  (log/info "Käynnistetään ajastetut eräajot")
  (qs/initialize)
  (log/info "Poistetaan vanhat jobit ennen uudelleenkäynnistystä")
  (qs/clear!)
  (qs/start)
  (log/info "Eräajomoottori käynnistetty")
  (let [eperusteet-job (j/build
                         (j/of-type PaivitaPerusteetJob)
                         (j/with-identity "paivita-perusteet")
                         (j/using-job-data {"asetukset" (:eperusteet-palvelu asetukset)}))
        eperusteet-trigger (t/build
                             (t/with-identity "eperusteet")
                             (t/start-now)
                             (t/with-schedule (ajastus asetukset :eperusteet)))]
    (qs/schedule eperusteet-job eperusteet-trigger)))
