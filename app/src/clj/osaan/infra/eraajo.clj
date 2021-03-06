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
            [osaan.infra.eraajo.tutkinnot :as etutkinto]
            [osaan.infra.eraajo.eperusteet :as eperusteet]
            osaan.infra.eraajo.eperusteet
            osaan.infra.eraajo.tutkinnot
            osaan.infra.eraajo.vanhat-arviot)
  (:import osaan.infra.eraajo.eperusteet.PaivitaPerusteetJob
           osaan.infra.eraajo.tutkinnot.PaivitaTutkinnotJob
           osaan.infra.eraajo.vanhat_arviot.PoistaVanhatArviotJob))

(defn ajastus [asetukset tyyppi]
  (cron/schedule
    (cron/cron-schedule (get-in asetukset [:ajastus tyyppi]))))

(def ajastin (promise))

(defn ^:integration-api kaynnista-ajastimet! [asetukset]
  (log/info "Käynnistetään ajastetut eräajot")
  (when-not (realized? ajastin)
    (deliver ajastin (qs/initialize)))
  (log/info "Poistetaan vanhat jobit ennen uudelleenkäynnistystä")
  (qs/clear! @ajastin)
  (qs/start @ajastin)
  (log/info "Eräajomoottori käynnistetty")
  ;; (etutkinto/paivita-tutkinnot! {:url "https://virkailija.opintopolku.fi/koodisto-service/rest/json/"})
  ;; (eperusteet/paivita-tutkintojen-perusteet {:url "https://virkailija.opintopolku.fi/eperusteet-service/"})
  (let [eperusteet-job (j/build
                         (j/of-type PaivitaPerusteetJob)
                         (j/with-identity "paivita-perusteet")
                         (j/using-job-data {"asetukset" (:eperusteet-palvelu asetukset)}))
        eperusteet-trigger (t/build
                             (t/with-identity "eperusteet")
                             (t/start-now)
                             (t/with-schedule (ajastus asetukset :eperusteet)))
        tutkinnot-job (j/build
                        (j/of-type PaivitaTutkinnotJob)
                        (j/with-identity "paivita-tutkinnot")
                        (j/using-job-data {"asetukset" (:koodistopalvelu asetukset)}))
        tutkinnot-trigger (t/build
                            (t/with-identity "tutkinnot")
                            (t/start-now)
                            (t/with-schedule (ajastus asetukset :koodistopalvelu)))
        arviot-job (j/build
                     (j/of-type PoistaVanhatArviotJob)
                     (j/with-identity "poista-vanhat-arviot")
                     (j/using-job-data {"asetukset" (:vanhat-arviot asetukset)}))
        arviot-trigger (t/build
                         (t/with-identity "arviot")
                         (t/start-now)
                         (t/with-schedule (ajastus asetukset :vanhat-arviot)))]
    (qs/schedule @ajastin eperusteet-job eperusteet-trigger)
    (qs/schedule @ajastin tutkinnot-job tutkinnot-trigger)
    (qs/schedule @ajastin arviot-job arviot-trigger)))
