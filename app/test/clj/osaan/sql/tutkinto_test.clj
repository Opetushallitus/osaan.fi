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

(ns osaan.sql.tutkinto-test
  (:require 
    [osaan.sql.test-util :refer :all]
    [osaan.arkisto.tutkinto :as tutkinto-db]
  )
  (:use clojure.test))

(use-fixtures :each tietokanta-fixture)

(deftest ^:integraatio testaahaku
  (let [ei-tulosta (tutkinto-db/hae-ehdoilla "huliviliveli" nil nil false)
        kaksi-perustetta (tutkinto-db/hae-ehdoilla "Aud" nil nil false)
        tutkintotason-suodatus (tutkinto-db/hae-ehdoilla "Käs" nil "erikoisammattitutkinto" false)
        tutkintotaso-eitulosta (tutkinto-db/hae-ehdoilla "Käs" nil "perustutkinto" false)
        ei-voimaantulevia (tutkinto-db/hae-ehdoilla nil nil nil nil)
        voimaantulevat (tutkinto-db/hae-ehdoilla nil nil nil true)]
    (is (= 0 (count ei-tulosta)))
    (is (= 2 (count kaksi-perustetta)))
    (is (= 1 (count tutkintotason-suodatus)))
    (is (= 0 (count tutkintotaso-eitulosta)))
    (is (= 4 (count voimaantulevat)))
    (is (= 3 (count ei-voimaantulevia)))))
    
