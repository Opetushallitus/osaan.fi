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

angular.module('osaan.raportti.raporttiui', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/raportti', {
        controller: 'RaporttiController',
        templateUrl: 'template/raportti/raportti.html',
        title: 'yleiset.raportti'
      });
  }])

  .factory('RaporttiApurit', [function() {
    return {
      listaaOsaamisalat: function(osaamisalat, tutkinnonosat) {
        if(osaamisalat.length === 0) {
          return [{tutkinnonosat: tutkinnonosat, osaamisalatunnus: undefined}];
        } else {
          return osaamisalat;
        }
      },
      valitseTutkinnonOsat: function(tutkinnonosat, valitutOsatunnukset) {
        return _(tutkinnonosat).filter(function(osa) { return valitutOsatunnukset.indexOf(osa.osatunnus) >= 0; }).uniq('osatunnus').value();
      },
      valitseOsaamisalat: function(osaamisalat, valitutOsaamisalat) {
        return _(osaamisalat).filter(function (ala) { return valitutOsaamisalat.indexOf(ala.osaamisalatunnus) >= 0; })
                             .map(function (ala) { return _.pick(ala, ['nimi_fi', 'nimi_sv']); })
                             .value();
      },
      valitseTutkinnonosatOsaamisaloittain: function(valitutOsat, osaamisalat, tutkinnonosat) {
        var output = {};
        _.forEach(osaamisalat, function(ala) {
          var osat = [];
          _.forEach(ala.tutkinnonosat, function(osa) {
            if(valitutOsat[ala.osaamisalatunnus] && valitutOsat[ala.osaamisalatunnus][osa.osatunnus]) {
              osat.push(tutkinnonosat[osa.osatunnus]);
            }
          });
          output[ala.osaamisalatunnus] = osat;
        });
        return output;
      }
    };
  }])

  .controller('RaporttiController', ['$filter', '$location', '$q', '$routeParams', '$scope', 'AmmattitaidonKuvaus', 'Arviointi', 'Osaamisala', 'Otsikko', 'Poistumisvaroitus', 'Raportti', 'RaporttiApurit', 'TekstiRaportti', 'Tutkinnonosa', 'Tutkinto', function($filter, $location, $q, $routeParams, $scope, AmmattitaidonKuvaus, Arviointi, Osaamisala, Otsikko, Poistumisvaroitus, Raportti, RaporttiApurit, TekstiRaportti, Tutkinnonosa, Tutkinto) {

    var valittuPeruste = Arviointi.valittuPeruste();
    if (valittuPeruste === undefined || !Arviointi.onkoArvioita()) {
      $location.url('/');
      return;
    }

    var tutkintoPromise = Tutkinto.haePerusteella(valittuPeruste);
    tutkintoPromise.then(function(tutkinto) {
      $scope.tutkinto = tutkinto;
      Otsikko.asetaAlaOtsikko($filter('lokalisoiKentta')(tutkinto, 'nimi'));
    });

    var tutkinnonosatPromise = Tutkinnonosa.hae(Arviointi.valittuPeruste(), Arviointi.valittuTutkintotunnus());

    var kohdealueetPromise = AmmattitaidonKuvaus.haeKohdealueetTutkinnonosille(Arviointi.valitutOsatunnukset());
    
    var osaamisalaPromise = Osaamisala.hae(valittuPeruste);

    $q.all({tutkinto: tutkintoPromise, tutkinnonosat: tutkinnonosatPromise, kohdealueet: kohdealueetPromise, osaamisalat: osaamisalaPromise}).then(function(tulokset) {
      $scope.osaamisalat = RaporttiApurit.listaaOsaamisalat(tulokset.osaamisalat, tulokset.tutkinnonosat);
      $scope.raportti = Raportti.luoRaportti(tulokset.tutkinto, RaporttiApurit.valitseTutkinnonOsat(_.flatten(_.map($scope.osaamisalat, 'tutkinnonosat')), Arviointi.valitutOsatunnukset()), tulokset.kohdealueet);
      var tutkinnonosat = _.zipObject(_.map($scope.raportti.tutkinnonosat, 'osatunnus'), $scope.raportti.tutkinnonosat);
      $scope.valitutTutkinnonosat = RaporttiApurit.valitseTutkinnonosatOsaamisaloittain(Arviointi.valitutOsat(), $scope.osaamisalat, tutkinnonosat);
      $scope.tekstiRaportti = TekstiRaportti.luoRaportti($scope.raportti);
      $scope.jakauma = _.map($scope.raportti.tutkinnonosat, function(osa) {
        return {
          arvo: osa.keskiarvo,
          nimi_fi: osa.nimi_fi,
          nimi_sv: osa.nimi_sv,
          vari: osa.pakollinen ? 0 : 1
        };
      });
    });

    $scope.vapaatekstiprint = '';
    $scope.jakauma = [];
    $scope.paivays = new Date();

    $scope.estaPoistumisvaroitus = function() {
      Poistumisvaroitus.estaPoistumisvaroitus();
    };

    $scope.onkoOsaamisalallaValittujaTutkinnonosia = function(osaamisala) {
      return _.size($scope.valitutTutkinnonosat[osaamisala.osaamisalatunnus]) > 0;
    };

    $scope.palaaArviointiin = function() {
      $location.url('/arviointi?osa=' + Arviointi.seuraavaOsatunnus());
    };
  }])
;
