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
  (:require [clojure.test :refer :all]
            [osaan.sql.test-util :refer :all]
            [osaan.arkisto.tutkinto :as tutkinto-db]))

(use-fixtures :each tietokanta-fixture)

; Audiovisuaalisen viestinnän perustutkinto (sv)
(deftest ^:integraatio testaahaku
  (let [ei-tulosta (tutkinto-db/hae-ehdoilla "huliviliveli" "fi" nil nil false)
        kaksi-perustetta (tutkinto-db/hae-ehdoilla "Aud" "fi" nil nil false)
        tutkintotason-suodatus (tutkinto-db/hae-ehdoilla "Käs" "fi" nil "erikoisammattitutkinto" false)
        tutkintotaso-eitulosta (tutkinto-db/hae-ehdoilla "Käs" "fi" nil "perustutkinto" false)
        ei-voimaantulevia (tutkinto-db/hae-ehdoilla nil "fi" nil nil nil)
        voimaantulevat (tutkinto-db/hae-ehdoilla nil "fi" nil nil true)
        ei-suomeksi (tutkinto-db/hae-ehdoilla "(sv)" "fi" nil nil nil)
        ruotsiksi (tutkinto-db/hae-ehdoilla "(sv)" "sv" nil nil nil)]
    (are [x y] (= x (count y))
      0 ei-tulosta
      2 kaksi-perustetta
      1 tutkintotason-suodatus
      0 tutkintotaso-eitulosta
      4 voimaantulevat
      3 ei-voimaantulevia
      0 ei-suomeksi
      3 ruotsiksi)))

(deftest ^:integraatio testaa-perustehaku
  (let [yksi (tutkinto-db/hae-perusteella -1)
        ei-tulosta (tutkinto-db/hae-perusteella -98765)]
    (is (nil? ei-tulosta))
    (is (some? yksi))))