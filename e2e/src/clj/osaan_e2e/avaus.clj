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

(ns osaan-e2e.avaus
  (:require [clj-webdriver.taxi :as w]
            [osaan-e2e.util :refer [avaa-url]]))

(defn osaan-url [polku]
  (str (or (System/getenv "OSAAN_URL")
           "http://192.168.50.1:8084")
       polku))

(defn avaa [polku]
  (avaa-url (osaan-url polku))
  (w/execute-script "angular.element(document.documentElement).data('$injector').get('$animate').enabled(false);")
  (w/execute-script "$('body').addClass('disable-all-animations');"))
