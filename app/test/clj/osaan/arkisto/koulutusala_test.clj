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

(ns osaan.arkisto.koulutusala-test
  (:require [clojure.test :refer :all]
            [osaan.arkisto.koulutusala :as arkisto]))

(def koulutusalat '({:nimi_sv "Kultur", :nimi_fi "Kulttuuriala", :koulutusalatunnus "6"}))

(def opintoalat '({:nimi_sv "Mediekultur och informationsvetenskaper", :nimi_fi "Viestintä ja informaatiotieteet", :koulutusala "6", :opintoalatunnus "202"}))

(def koulutusalat-opintoaloittain '({:opintoalat ({:nimi_sv "Mediekultur och informationsvetenskaper", :nimi_fi "Viestintä ja informaatiotieteet", :opintoalatunnus "202"}), :nimi_sv "Kultur", :nimi_fi "Kulttuuriala", :koulutusalatunnus "6"}))

(deftest hae-koulutusalat-opintoaloilla
  []
  (let [koulutusalat (with-redefs [osaan.arkisto.koulutusala/hae-koulutusalat (fn [] koulutusalat)
                                   osaan.arkisto.koulutusala/hae-opintoalat (fn [] opintoalat)]
    (arkisto/hae-koulutusalat-opintoaloilla))]
    (is (= koulutusalat koulutusalat-opintoaloittain))))
