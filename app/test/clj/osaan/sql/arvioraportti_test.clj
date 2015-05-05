(ns osaan.sql.arvioraportti_test
  (:require 
    [osaan.sql.test-util :refer :all]
    [osaan.arkisto.arvioraportti :as arvio-db]
  )
  (:use clojure.test))

(use-fixtures :each tietokanta-fixture)

(deftest ^:integraatio testaahaku
  (let [tulos (arvio-db/hae-arvio "testiarvio")]
    (is (= 5 (count tulos)))))