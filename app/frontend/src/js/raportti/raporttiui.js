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
        templateUrl: 'template/raportti/raportti.html'
      });
  }])

  .factory('RaporttiApurit', [function() {
    return {
      valitseTutkinnonOsat: function(tutkinnonosat, valitutOsatunnukset) {
        return _(tutkinnonosat).groupBy('osatunnus').pick(valitutOsatunnukset).values().flatten().value();
      }
    };
  }])

  .controller('RaporttiController', ['$location', '$q', '$routeParams', '$scope', 'AmmattitaidonKuvaus', 'Arviointi', 'Poistumisvaroitus', 'Raportti', 'RaporttiApurit', 'TekstiRaportti', 'Tutkinnonosa', 'Tutkinto', function($location, $q, $routeParams, $scope, AmmattitaidonKuvaus, Arviointi, Poistumisvaroitus, Raportti, RaporttiApurit, TekstiRaportti, Tutkinnonosa, Tutkinto) {
    $scope.valittuRaportti = 'raportti';

    var tutkintoPromise = Tutkinto.haePerusteella(Arviointi.valittuPeruste());
    tutkintoPromise.then(function(tutkinto) {
      $scope.tutkinto = tutkinto;
    });

    var tutkinnonosatPromise = Tutkinnonosa.hae(Arviointi.valittuPeruste(), Arviointi.valittuTutkintotunnus())
      .then(function(tutkinnonosat) {
        return RaporttiApurit.valitseTutkinnonOsat(tutkinnonosat, Arviointi.valitutOsatunnukset());
      });

    var kohdealueetPromise = AmmattitaidonKuvaus.haeKohdealueetTutkinnonosille(Arviointi.valitutOsatunnukset());

    $q.all({tutkinto: tutkintoPromise, tutkinnonosat: tutkinnonosatPromise, kohdealueet: kohdealueetPromise}).then(function(tulokset) {
      $scope.raportti = Raportti.luoRaportti(tulokset.tutkinto, tulokset.tutkinnonosat, tulokset.kohdealueet);
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

    $scope.jakauma = [];
    $scope.paivays = new Date();

    $scope.estaPoistumisvaroitus = function() {
      Poistumisvaroitus.estaPoistumisvaroitus();
    };

    $scope.palaaArviointiin = function() {
      $location.url('/arviointi?osa=' + Arviointi.seuraavaOsatunnus());
    };
  }])
;
