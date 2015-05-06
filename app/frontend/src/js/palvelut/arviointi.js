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
      'osatunnukset': []
    };
    var _tallennaTila = function() {
      sessionStorage.setItem('arviointi', JSON.stringify(tila));
    };

    // Lataa tila sessionStoragesta servicen alustuksessa
    if (sessionStorage.getItem('arviointi') !== null) {
      tila = JSON.parse(sessionStorage.getItem('arviointi'));
    }

    var asetaOsatunnukset = function(osat) {
      tila.osatunnukset = osat;
      _tallennaTila();
    };

    var asetaTutkintoJaPeruste = function(tutkintotunnus, peruste) {
      tila.tutkintotunnus = tutkintotunnus;
      tila.peruste = peruste;
      _tallennaTila();
    };

    var edellinenOsatunnus = function(osatunnus) {
      var index = tila.osatunnukset.indexOf(osatunnus);
      if (index > 0) {
        return tila.osatunnukset[index - 1];
      }

      return undefined;
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

    var valittuPeruste = function() { return tila.peruste; };
    var valittuTutkintotunnus = function() { return tila.tutkintotunnus; };
    var valitutOsatunnukset = function() { return tila.osatunnukset; };

    return {
      asetaOsatunnukset: asetaOsatunnukset,
      asetaTutkintoJaPeruste: asetaTutkintoJaPeruste,
      edellinenOsatunnus: edellinenOsatunnus,
      seuraavaOsatunnus: seuraavaOsatunnus,
      valittuPeruste: valittuPeruste,
      valittuTutkintotunnus: valittuTutkintotunnus,
      valitutOsatunnukset: valitutOsatunnukset
    };
  }])
;
