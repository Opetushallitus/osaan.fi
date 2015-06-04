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

(ns osaan.rest-api.kaiku
  (:require [compojure.core :as c]
            [oph.common.util.http-util :refer [json-response]]
            [osaan.compojure-util :as cu]))

(c/defroutes reitit
  (cu/defapi :julkinen nil :post "/" request
    (let [data (-> request :form-params (get "data"))
          filename (-> request :form-params (get "filename"))]
      {:status  200
       :headers {"Content-Type"        "text/plain"
                 "Content-Disposition" (str "attachment; filename=" filename)}
       :body    data})))