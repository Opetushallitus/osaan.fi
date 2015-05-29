// Copyright (c) 2015 The Finnish National Board of Education - Opetushallitus
//
// This program is free software:  Licensed under the EUPL, Version 1.1 or - as
// soon as they will be approved by the European Commission - subsequent versions
// of the EUPL (the "Licence");
//
// You may not use this work except in compliance with the Licence.
// You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// European Union Public Licence for more details.

'use strict';

angular.module('osaan.palvelut.arviointi', [])

  .factory('Arviointi', [function() {
    var tila = {
      arviot: {}, // tutkinnonosatunnus -> ammattitaidonkuvaus_id -> {arvio, vapaateksti}
      osatunnukset: []
    };
    var ladattu = true;
    var _tallennaTila = function() {
      sessionStorage.setItem('arviointi', JSON.stringify(tila));
      ladattu = false;
    };

    // Lataa tila sessionStoragesta servicen alustuksessa
    if (sessionStorage.getItem('arviointi') !== null) {
      tila = JSON.parse(sessionStorage.getItem('arviointi'));
    }

    var asetaArviot = function(tutkinnonosatunnus, arviot) {
      if (!_.isEqual(tila.arviot[tutkinnonosatunnus], arviot)) {
        tila.arviot[tutkinnonosatunnus] = angular.copy(arviot);
        _tallennaTila();
      }
    };

    var asetaLadatuksi = function() {
      ladattu = true;
    };

    var asetaOsatunnukset = function(osat) {
      if (!_.isEqual(tila.osatunnukset, osat)) {
        tila.osatunnukset = angular.copy(osat);
        _tallennaTila();
      }
    };

    var asetaTutkintoJaPeruste = function(tutkintotunnus, peruste) {
      if (tutkintotunnus !== tila.tutkintotunnus || peruste !== tila.peruste) {
        tila.tutkintotunnus = tutkintotunnus;
        tila.peruste = peruste;
        _tallennaTila();
      }
    };

    var edellinenOsatunnus = function(osatunnus) {
      var index = tila.osatunnukset.indexOf(osatunnus);
      if (index > 0) {
        return tila.osatunnukset[index - 1];
      }

      return undefined;
    };

    var haeArviot = function(tutkinnonosatunnus) {
      return angular.copy(tila.arviot[tutkinnonosatunnus]);
    };

    var lataa = function(uusiTila) {
      asetaTutkintoJaPeruste(uusiTila.tutkintotunnus, String(uusiTila.peruste));
      asetaOsatunnukset(uusiTila.tutkinnonosat);
      _.forEach(uusiTila.kohdearviot, function(arviot, tutkinnonosatunnus) {
        asetaArviot(tutkinnonosatunnus, arviot);
      });
      ladattu = true;
    };

    var onkoArvioita = function(osatunnusOption) {
      // Tarkistetaan vain valitut osatunnukset
      var osatunnukset = _.intersection(_.keys(tila.arviot), tila.osatunnukset);
      var onko = false;
      _.forEach(osatunnukset, function(osatunnus) {
        if (_.keys(tila.arviot[osatunnus]).length > 0 && (osatunnusOption === undefined || osatunnus === osatunnusOption)) {
          onko = true;
        }
      });
      return onko;
    };

    var onkoLadattu = function() {
      return ladattu;
    };

    var poistaArviot = function(tutkinnonosatunnus) {
      delete tila.arviot[tutkinnonosatunnus];
      _tallennaTila();
    };

    var seuraavaOsatunnus = function(osatunnus) {
      if (osatunnus === undefined) {
        return tila.osatunnukset[0];
      }
      var index = tila.osatunnukset.indexOf(osatunnus);
      if (tila.osatunnukset.length > index) {
        return tila.osatunnukset[index + 1];
      }
      return undefined;
    };

    var tyhjennaArviot = function() {
      tila.arviot = {};
    };

    var valittuPeruste = function() { return tila.peruste; };
    var valittuTutkintotunnus = function() { return tila.tutkintotunnus; };
    var valitutOsatunnukset = function() { return angular.copy(tila.osatunnukset); };

    return {
      asetaArviot: asetaArviot,
      asetaLadatuksi: asetaLadatuksi,
      asetaOsatunnukset: asetaOsatunnukset,
      asetaTutkintoJaPeruste: asetaTutkintoJaPeruste,
      edellinenOsatunnus: edellinenOsatunnus,
      haeArviot: haeArviot,
      lataa: lataa,
      onkoArvioita: onkoArvioita,
      onkoLadattu: onkoLadattu,
      poistaArviot: poistaArviot,
      seuraavaOsatunnus: seuraavaOsatunnus,
      tyhjennaArviot: tyhjennaArviot,
      valittuPeruste: valittuPeruste,
      valittuTutkintotunnus: valittuTutkintotunnus,
      valitutOsatunnukset: valitutOsatunnukset
    };
  }])
;
