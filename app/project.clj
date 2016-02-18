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
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/tools.logging "0.3.1"]

                 [cheshire "5.5.0"]
                 [ch.qos.logback/logback-classic "1.1.5"]
                 [clj-http "2.1.0"] ; clojure-utils
                 [clj-time "0.11.0"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [compojure "1.4.0"]
                 [com.cemerick/valip "0.3.2"] ; clojure-utils
                 [com.jolbox/bonecp "0.8.0.RELEASE"]
                 [http-kit "2.1.19"]
                 [korma "0.4.2"]
                 [metosin/compojure-api "1.0.0"]
                 [org.slf4j/slf4j-api "1.7.16"]
                 [org.postgresql/postgresql "9.4-1206-jdbc42"]
                 [peridot "0.4.3"]
                 [stencil "0.5.0"]
                 [prismatic/schema "1.0.5"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-headers "0.1.3"]
                 [ring/ring-json "0.4.0"]]
  :plugins [[test2junit "1.0.1"]
            [jonase/eastwood "0.2.3"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/test.check "0.7.0"]
                                  [org.clojure/tools.namespace "0.2.10"]

                                  [clj-gatling "0.5.4"]
                                  [clj-webdriver "0.6.1"]
                                  [ring/ring-mock "0.2.0"]]}
             :uberjar {:main osaan.palvelin
                       :aot :all}
             :test {:resource-paths ["test-resources"]}}
  :source-paths ["src/clj" "clojure-utils/src/clj"]
  :java-source-paths ["src/java" "clojure-utils/src/java"]
  :javac-options ["-target" "1.7" "-source" "1.7"]
  :test-paths ["test/clj"]
  :test-selectors {:default  (complement (some-fn :integraatio :performance))
                   :integraatio (complement (some-fn :performance))
                   :performance :performance}
  :jar-name "osaan.jar"
  :uberjar-name "osaan-standalone.jar"
  :main osaan.palvelin
  :repl-options {:init-ns user})
