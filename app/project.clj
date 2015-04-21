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

(defproject osaan "0.1.0-SNAPSHOT"
  :description "osaan.fi"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/tools.logging "0.3.1"]

                 [cheshire "5.4.0"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [clj-http "1.0.1"]
                 [clj-time "0.9.0"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [compojure "1.3.3"]
                 [com.cemerick/valip "0.3.2"]
                 [com.jolbox/bonecp "0.8.0.RELEASE"]
                 [http-kit "2.1.19"]
                 [korma "0.4.0"]
                 [metosin/compojure-api "0.19.3"]
                 [metosin/ring-swagger-ui "2.0.24"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [peridot "0.3.1"]
                 [stencil "0.3.5"]
                 [prismatic/schema "0.4.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-headers "0.1.2"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-session-timeout "0.1.0"]
                 [robert/hooke "1.3.0"]]
  :plugins [[test2junit "1.0.1"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/test.check "0.7.0"]
                                  [org.clojure/tools.namespace "0.2.10"]

                                  [clj-gatling "0.5.4"]
                                  [clj-webdriver "0.6.1"]
                                  [ring/ring-mock "0.2.0"]]}
             :uberjar {:main osaan.palvelin
                       :aot :all}
             :test {:resource-paths ["test-resources"]}}
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.7" "-source" "1.7"]
  :test-paths ["test/clj"]
  :test-selectors {:default  (complement (some-fn :integraatio :performance))
                   :integraatio (complement (some-fn :performance))
                   :performance :performance}
  :jar-name "osaan.jar"
  :uberjar-name "osaan-standalone.jar"
  :main osaan.palvelin
  :repl-options {:init-ns user})
