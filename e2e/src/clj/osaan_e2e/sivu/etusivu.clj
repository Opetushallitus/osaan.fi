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

(ns osaan-e2e.sivu.etusivu
  (:require [clj-webdriver.taxi :as w]
            [osaan-e2e.avaus :as avaus]
            [osaan-e2e.util :refer :all]))

(def etusivu "/#/")

(defn avaa-sivu []
  (avaus/avaa etusivu))

(defn sivun-sisalto []
  (w/text (w/find-element {:css "body"})))

(defn aseta-tutkinnon-nimi [nimi]
  (odota-kunnes (w/visible? {:css ".e2e-tutkinnon-nimi"}))
  (w/input-text {:css ".e2e-tutkinnon-nimi"} nimi))

(defn valitse-tutkinto
  [nimi]
  (odota-kunnes (w/visible? {:css ".e2e-haettu-tutkinto"}))
  (w/click {:css ".e2e-haettu-tutkinto"}))
