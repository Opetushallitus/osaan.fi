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

(ns osaan.skeema
  (:require [schema.core :as s]))

(def Arvio {:tutkintotunnus s/Str
            :peruste s/Int
            ; tutkinnonosatunnus -> ammattitaidonkuvaus_id -> { arvio, vapaateksti }
            :kohdearviot {s/Any {s/Any {(s/optional-key :arvio) (s/maybe s/Int)
                                        (s/optional-key :vapaateksti) (s/maybe (s/conditional #(<= (count %) 10000) String))}}}
            :tutkinnonosat [s/Str]})

(def ArvioUlos (assoc Arvio (s/optional-key :luotuaika) java.sql.Timestamp))

(def AmmattitaidonKuvaus {:ammattitaidonkuvaus_id s/Int
                          :nimi_fi s/Str
                          :nimi_sv (s/maybe s/Str)})

(def ArvioinninKohdealue {:arvioinninkohdealue_id s/Int
                          :nimi_fi s/Str
                          :nimi_sv (s/maybe s/Str)
                          :kuvaukset [AmmattitaidonKuvaus]})

(def Opintoala {:nimi_fi s/Str
                :nimi_sv s/Str
                :opintoalatunnus s/Str})

(def Koulutusala {:koulutusalatunnus s/Str
                  :nimi_fi s/Str
                  :nimi_sv s/Str
                  :opintoalat [Opintoala]})

(def ^:private TutkintoPerustiedot {:nimi_fi s/Str
                                    :nimi_sv (s/maybe s/Str)
                                    :opintoala_nimi_fi s/Str
                                    :opintoala_nimi_sv (s/maybe s/Str)
                                    :peruste_id s/Int
                                    :peruste_diaarinumero s/Str
                                    :peruste_tyyppi s/Str
                                    :peruste_eperustetunnus s/Int
                                    :tutkintotunnus s/Str})

(def Tutkinto (merge TutkintoPerustiedot
                     {:koulutusala_nimi_fi s/Str
                      :koulutusala_nimi_sv (s/maybe s/Str)}))

(def TutkintoHakutulos TutkintoPerustiedot)

(def Tutkinnonosa {:nimi_fi s/Str
                   :nimi_sv (s/maybe s/Str)
                   :osatunnus s/Str
                   :tyyppi s/Str})

(def Tutkinnonosa->ArvioinninKohdealueet {s/Str [ArvioinninKohdealue]})
