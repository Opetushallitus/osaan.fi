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

(ns osaan.infra.kayttaja.sql
  (:require [korma.core :as sql]
            [korma.db :as db]
            [clojure.tools.logging :as log]))

(defn with-sql-kayttaja* [oid f]
  (db/transaction
    (try
      (sql/exec-raw (str "set osaan.kayttaja = '" oid "';"))
      (f)
      (catch Throwable t
        (log/error "Virhe tietokantafunktiossa" t)
        (throw t))
      (finally
        (sql/exec-raw (str "reset osaan.kayttaja;"))))))

(defmacro with-sql-kayttaja [oid & body]
  `(with-sql-kayttaja* ~oid (fn [] ~@body)))
