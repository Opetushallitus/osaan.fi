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

(ns osaan.compojure-util
  (:require [oph.compojure-util :as oph-cjure]
            [osaan.toimiala.kayttajaoikeudet :as ko]))

(defmacro defapi
  "Esittelee rajapinta-funktion sisältäen käyttöoikeuksien tarkastamisen. Olettaa, että ollaan transaktion sisällä."
  [toiminto konteksti-arg http-method path args & body]
  (let [auth-map ko/toiminnot]
    `(oph-cjure/defapi-within-transaction ~auth-map ~toiminto ~konteksti-arg ~http-method ~path ~args ~@body)))
