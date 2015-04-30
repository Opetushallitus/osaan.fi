;; Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
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

(defproject aipal-e2e "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [korma "0.4.0"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [solita/opetushallitus-aitu-e2e "0.21.0"]
                 [clj-time "0.9.0"]]
  :plugins [[test2junit "1.0.1"]]

  :source-paths ["src/clj" "../app/src/clj" "../../clojure-utils/src/clj"]
  :test-paths ["test/clj"]

  :test-selectors {:ie (complement :no-ie)})

