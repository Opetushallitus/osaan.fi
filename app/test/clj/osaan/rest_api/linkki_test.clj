(ns osaan.rest-api.linkki-test
  (:require [clojure.test :refer :all]
            [osaan.rest-api.session-util :refer :all]
            [osaan.sql.test-util :refer [tietokanta-fixture]]))

(use-fixtures :each tietokanta-fixture)

(deftest ^:integraatio linkki-ulkoa
  (let [crout (init-peridot!)]
    ; http://localhost:8084/linkki/osien-valinta?tutkinto=324601&peruste=-1&tyyppi=naytto&diaarinumero=41/011/2005
    ; http://localhost:8084/linkki/osien-valinta?tutkinto=324601&peruste=-1&tyyppi=naytto&diaarinumero=38/011/2014
    (let [toimiva {"tutkinto" 324601
                   "peruste" -1
                   "tyyppi" "naytto"
                   "diaarinumero" "38/011/2014"}
          vanhentunut-peruste {"tutkinto" 324601
                               "peruste" -1
                               "tyyppi" "naytto"
                               "diaarinumero" "41/011/2005"}]
      (let [r-toimiva (mock-request! crout "/linkki/osien-valinta" :get toimiva)
            r-vanhentunut (mock-request! crout "/linkki/osien-valinta" :get vanhentunut-peruste)]
        
        (is (= (:status (:response r-toimiva)) 302))
        (is (= (get-in r-toimiva [:response :headers "Location"]) "http://localhost:80/#/osien-valinta?tutkinto=324601&peruste=-2"))
        (is (= (:status (:response r-vanhentunut)) 400))
        (is (= (:body (:response r-vanhentunut)) "Tutkintoa tai sen perusteita ei löydy."))))))
  