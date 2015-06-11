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

angular.module('osaan.palvelut.raportti', [])

  .factory('Raportti', ['Arviointi', function(Arviointi) {
    var service = {};

    var yhdistaRakenteet = function(tutkinto, tutkinnonosat, kohdealueet) {
      var raportti = _.pick(tutkinto, ['koulutusala_nimi_fi', 'koulutusala_nimi_sv', 'opintoala_nimi_fi', 'opintoala_nimi_sv']);
      _.merge(raportti, {
        tutkinto_nimi_fi: tutkinto.nimi_fi,
        tutkinto_nimi_sv: tutkinto.nimi_sv
      });

      raportti.tutkinnonosat = _.map(tutkinnonosat, function(tutkinnonosa_) {
        var tutkinnonosa = angular.copy(tutkinnonosa_);
        tutkinnonosa.kohdealueet = _.map(kohdealueet[tutkinnonosa.osatunnus], function(kohdealue) {
          return angular.copy(kohdealue);
        });
        return tutkinnonosa;
      });

      return raportti;
    };

    service.luoRaportti = function(tutkinto, tutkinnonosat, kohdealueet) {
      var raportti = yhdistaRakenteet(tutkinto, tutkinnonosat, kohdealueet);

      // Nido arviot rakenteeseen ja laske keskiarvot eri tasoille
      var tutkintoSumma = 0;
      var tutkintoArvioita = 0;

      _.forEach(raportti.tutkinnonosat, function(tutkinnonosa) {
        var arviot = Arviointi.haeArviot(tutkinnonosa.osatunnus);
        var arvioidutAmmattitaidonKuvausIdt = _.map(_.keys(arviot), function(x) { return parseInt(x); });
        var tutkinnonosaSumma = 0;
        var tutkinnonosaArvioita = 0;

        _.forEach(tutkinnonosa.kohdealueet, function(kohdealue) {
          var ammattitaidonKuvausIdt = _.pluck(kohdealue.kuvaukset, 'ammattitaidonkuvaus_id');
          var kohdealueenArvioIdt = _.intersection(arvioidutAmmattitaidonKuvausIdt, ammattitaidonKuvausIdt);
          var kohdealueenArviot = _.filter(_.map(kohdealueenArvioIdt, function(id) { return arviot[id].arvio; }), _.isNumber);
          var summa = _.reduce(kohdealueenArviot, function(total, n) { return total + n; }, 0);
          var arvioita = kohdealueenArviot.length;
          tutkinnonosaSumma += summa;
          tutkinnonosaArvioita += arvioita;

          _.forEach(kohdealue.kuvaukset, function(kuvaus) {
            if (arviot[kuvaus.ammattitaidonkuvaus_id] !== undefined) {
              kuvaus.arvio = angular.copy(arviot[kuvaus.ammattitaidonkuvaus_id]);
            }
          });

          kohdealue.keskiarvo = arvioita > 0 ? (summa / arvioita) : 0;
        });

        var osanKuvaukset = _(tutkinnonosa.kohdealueet).map('kuvaukset').flatten();
        tutkinnonosa.arvioita = osanKuvaukset.filter('arvio').map('arvio').filter(function(arvio) { return arvio.arvio !== undefined; }).value().length;
        tutkinnonosa.arvioitavia = osanKuvaukset.flatten().value().length;
        tutkinnonosa.arvioituna = Math.floor((100 * tutkinnonosa.arvioita) / tutkinnonosa.arvioitavia);
        tutkinnonosa.keskiarvo = tutkinnonosaArvioita > 0 ? (tutkinnonosaSumma / tutkinnonosaArvioita) : 0;

        tutkintoSumma += tutkinnonosaSumma;
        tutkintoArvioita += tutkinnonosaArvioita;
      });

      raportti.keskiarvo = tutkintoArvioita > 0 ? (tutkintoSumma / tutkintoArvioita) : 0;

      return raportti;
    };

    return service;
  }])
;
