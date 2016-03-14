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
      listaaTutkinnonosat: function(osaamisalat, tutkinnonosat) {
        if(osaamisalat.length === 0) {
          return tutkinnonosat;
        } else {
          return _.flatten(_.map(osaamisalat, 'tutkinnonosat'));
        }
      },
      valitseTutkinnonOsat: function(tutkinnonosat, valitutOsatunnukset) {
        return _(tutkinnonosat).filter(function(osa) { return valitutOsatunnukset.indexOf(osa.osatunnus) >= 0; }).value();
      },
      valitseOsaamisalat: function(osaamisalat, valitutOsaamisalat) {
        return _(osaamisalat).filter(function (ala) { return valitutOsaamisalat.indexOf(ala.osaamisalatunnus) >= 0; })
                             .map(function (ala) { return _.pick(ala, ['nimi_fi', 'nimi_sv']); })
                             .value();
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
      $scope.kaikkiTutkinnonosat = RaporttiApurit.listaaTutkinnonosat(tulokset.osaamisalat, tulokset.tutkinnonosat);
      $scope.valitutOsaamisalat = RaporttiApurit.valitseOsaamisalat(tulokset.osaamisalat, Arviointi.valitutOsaamisalat());
      $scope.raportti = Raportti.luoRaportti(tulokset.tutkinto, RaporttiApurit.valitseTutkinnonOsat($scope.kaikkiTutkinnonosat, Arviointi.valitutOsatunnukset()), tulokset.kohdealueet);
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
