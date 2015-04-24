(ns osaan.sql.ohje
  (:require 
    [osaan.sql.test-util :refer :all]
    [osaan.arkisto.ohje :as ohje-db]
  )
  (:use clojure.test))

(use-fixtures :each tietokanta-fixture)

(deftest ^:integraatio hae-puuttuva
  (is (thrown? Throwable
               (ohje-db/hae "futil"))))
