;; Copyright (c) 2013 The Finnish National Board of Education - Opetushallitus
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

(ns osaan-db.core
  (:gen-class)
  (:import [com.googlecode.flyway.core Flyway]
           [com.googlecode.flyway.core.util.jdbc DriverDataSource]
           [javax.sql DataSource]
           [java.net URLEncoder
                     URLDecoder]
           com.googlecode.flyway.core.api.FlywayException)
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [stencil.core :as s]
            [clojure.tools.cli :refer [parse-opts]]))


(defn jdbc-do
  [& sql]
  (doseq [stm sql]
    (println (str stm ";")))
  (apply jdbc/do-commands sql))

(defn sql-tiedostosta
  "Palauttaa vektorin, jossa on SQL-statementteja.
   TODO: parseri ei ole mitenkään täydellinen.."
  [nimi]
  (clojure.string/split (slurp nimi) #";"))

(defn sql-resurssista [nimi]
  (sql-tiedostosta (io/resource nimi)))

(defn run-sql [sql]
  (try
    (jdbc-do "set session osaan.kayttaja='JARJESTELMA'")
    (doseq [stmt sql]
      (jdbc-do stmt))
    (catch java.sql.SQLException e
      (throw (.getNextException e)))
    (finally
      (jdbc-do "set osaan.kayttaja to default"))))

(defn prefix [s n]
  (.substring s 0 (min (.length s) n)))

(defn aseta-oikeudet-sovelluskayttajalle
  [username]
  (jdbc-do
    (str "GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO " username )
    (str "GRANT SELECT, USAGE ON ALL SEQUENCES IN SCHEMA public TO " username )))

(defn luo-testidata!
  []
  (run-sql (sql-resurssista "sql/testidata.sql") ))

(defn parse-uri
  "Parsitaan mappiin Postgren JDBC-URL.
   Postgren JDBC-ajuri palauttaa null Connection-olioita jos URL sisältää usernamen/passwordin."
  [uri]
  (let [prefix (first (clojure.string/split uri #"//"))
        etc (second (clojure.string/split uri #"//"))
        user (URLDecoder/decode (first (clojure.string/split etc #":")))
        passwd (URLDecoder/decode (first (clojure.string/split (second (clojure.string/split etc #":")) #"@")))
        postfix (second (clojure.string/split etc #"@"))]
     {:user user
      :passwd passwd
      :uri uri
      :postgre-uri (str "jdbc:" prefix "//" postfix)
      }))

(defn create-datasource!
  "Palauttaa Flyway DataSourcen, jota voidaan käyttää myös JDBC:n kanssa"
  [url]
  (let [jdbc-creds (parse-uri url)
        datasource (DriverDataSource.
                     nil (:postgre-uri jdbc-creds) (:user jdbc-creds) (:passwd jdbc-creds)
                     (into-array ["set session osaan.kayttaja='JARJESTELMA';"]))]
    datasource))

(defn alusta-flywaylla!
  "Alustaa tietokannan Flywayn avulla."
  [datasource options]
  (let [flyway (Flyway.)
        kantaversio (:target-version options)
        tyhjenna (:clear options)]
    (doto flyway
      (.setDataSource datasource)
      (.setLocations (into-array String ["/db/migration"]))
      (cond->
        tyhjenna (.clean)
        kantaversio (.setTarget kantaversio))
      (.setPlaceholders {"osaan_user" (:username options)})
      (.migrate))))

(def cli-options
  [[nil "--clear" "Tyhjennetään kanta ja luodaan skeema ja pohjadata uusiksi"
    :default false]
   [nil "--target-version VERSION" "Tehdään migrate annettuun versioon saakka"]
   ["-t" nil "Testidatan luonti"
    :id :testidata]
   ["-s" "--sql SQL" "Tiedosto, jonka sisältämät SQL-lauseet suoritetaan migraation päätteeksi"
    :assoc-fn #(update-in %1 [%2] (fnil conj []) %3)]
   ["-u" "--username USER" "Tietokantakäyttäjä"
    :default "osaan_user"]
   ["-h" "--help" "Käyttöohje"]])

(defn ohje [options-summary]
  (->> ["Käyttö: lein run [options] [jdbc-url]"
        ""
        "Käyttöönotettaessa uusi kanta on tyhjennettävä (--clear), jotta voidaan varmistua että kanta on"
        "tyhjä eikä sisällä ylimääräisiä tauluja tai dataa. jdbc-url:ssa käyttäjä ja salasana tulee olla URL enkoodattu"
        ""
        "Ilman jdbc-url osoitetta yritetään lukea osoite osaan.properties tiedoston perusteella."
        ""
        "Optiot:"
        options-summary
        ""]
       (clojure.string/join \newline)))

(defn error-msg [errors]
  (str "Virhe parametreissa:\n\n"
       (clojure.string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn properties->jdbc-url
 [reader]
 (let [props  (doto (java.util.Properties.)
                (.load reader))
       host (.getProperty props "db.host")
       schema-user (.getProperty props "db.user")
       schema-name (.getProperty props "db.name")
       schema-user-passwd (.getProperty props "db.password")
       port (.getProperty props "db.port")]
   (str "postgresql://" (URLEncoder/encode schema-user) ":" (URLEncoder/encode schema-user-passwd) "@" host ":" port "/" schema-name)))

(defn file->jdbc-url
  [polku]
  (with-open [reader (clojure.java.io/reader polku)]
    (properties->jdbc-url reader)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (ohje summary))
      (> (count arguments) 1) (exit 1 (ohje summary))
      errors (exit 1 (error-msg errors)))
    (let [jdbc-url (or (first arguments) (file->jdbc-url "oph-configuration/osaan-db.properties"))
          datasource (create-datasource! jdbc-url)
          db-spec {:datasource datasource}
          migraatiopoikkeus (try
                              (alusta-flywaylla! datasource options)
                              nil
                              (catch FlywayException e
                                e))]
      (try
        (jdbc/with-connection db-spec
          (println "Annetaan käyttöoikeudet sovelluskäyttäjälle, vaikka osa migraatioista epäonnistuisi.")
          (aseta-oikeudet-sovelluskayttajalle (:username options))
          (when (:testidata options)
            (println "luodaan testidata")
            (luo-testidata!))
          (doseq [s (:sql options)]
            (run-sql (sql-tiedostosta s))))
        (finally
          (when migraatiopoikkeus
            (println "!!!! TAPAHTUI VIRHE !!!")
            (.printStackTrace migraatiopoikkeus System/out)
            (System/exit 1)))))))
