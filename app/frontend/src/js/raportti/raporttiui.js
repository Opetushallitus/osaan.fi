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
      arvioidenKeskiarvo: function(kuvauksenArvio) {
        var arviot = _.chain(kuvauksenArvio)
          .values()
          .pluck('arvio')
          .filter(_.isNumber)
          .value();
        var summa = _.reduce(arviot, function add(result, x) {return result + x;});
        var n = arviot.length;

        return n >= 1 ? summa / n : 0;
      },

      liitaTutkinnonOsiinArviot: function(osatunnukset, haeArvio) {
        return _.zipObject(
          _.map(osatunnukset, function(osatunnus) {return [osatunnus, haeArvio(osatunnus)];})
        );
      },

      muodostaJakauma: function(tutkinnonosat, tutkinnonOsanTulos) {
        return _.map(
          tutkinnonosat,
          function(tutkinnonosa) {
            return _.merge(_.pick(tutkinnonosa, ['nimi_fi', 'nimi_sv']), {
              arvo: tutkinnonOsanTulos[tutkinnonosa.osatunnus],
              vari: tutkinnonosa.pakollinen ? 0 : 1
            });
          }
        );
      },

      valitseTutkinnonOsat: function(tutkinnonosat, valitutOsatunnukset) {
        return _(tutkinnonosat).groupBy('osatunnus').pick(valitutOsatunnukset).values().flatten().value();
      }
    };
  }])

  .controller('RaporttiController', ['$location', '$q', '$routeParams', '$scope', 'AmmattitaidonKuvaus', 'Arviointi', 'RaporttiApurit', 'TekstiRaportti', 'Tutkinnonosa', 'Tutkinto', function($location, $q, $routeParams, $scope, AmmattitaidonKuvaus, Arviointi, RaporttiApurit, TekstiRaportti, Tutkinnonosa, Tutkinto) {
    $scope.valittuRaportti = 'raportti';

    var tutkintoPromise = Tutkinto.haePerusteella(Arviointi.valittuPeruste());
    tutkintoPromise.then(function(tutkinto) {
      $scope.tutkinto = tutkinto;
    });

    var tutkinnonosatPromise = Tutkinnonosa.hae(Arviointi.valittuPeruste(), Arviointi.valittuTutkintotunnus())
      .then(function(tutkinnonosat) {
        $scope.tutkinnonosat = RaporttiApurit.valitseTutkinnonOsat(tutkinnonosat, Arviointi.valitutOsatunnukset());
        return $scope.tutkinnonosat;
      });
    tutkinnonosatPromise
      .then(function(tutkinnonosat) {
        var tutkinnonOsanTulos = _.mapValues($scope.arviot, RaporttiApurit.arvioidenKeskiarvo);
        $scope.jakauma = RaporttiApurit.muodostaJakauma(tutkinnonosat, tutkinnonOsanTulos);
      });

    var kohdealueetPromise = AmmattitaidonKuvaus.haeKohdealueetTutkinnonosille(Arviointi.valitutOsatunnukset());
    kohdealueetPromise
      .then(function(kohdealueet) {
        $scope.kohdealueet = kohdealueet;
      });

    $q.all({tutkinto: tutkintoPromise, tutkinnonosat: tutkinnonosatPromise, kohdealueet: kohdealueetPromise}).then(function(tulokset) {
      $scope.tekstiRaportti = TekstiRaportti.luoRaportti(tulokset.tutkinto, tulokset.tutkinnonosat, tulokset.kohdealueet);
    });

    $scope.arviot = RaporttiApurit.liitaTutkinnonOsiinArviot(Arviointi.valitutOsatunnukset(),
      function(osatunnus) {return Arviointi.haeArviot(osatunnus);}
    );
    $scope.jakauma = [];
    $scope.paivays = new Date();

    $scope.palaaArviointiin = function() {
      $location.url('/arviointi?osa=' + Arviointi.seuraavaOsatunnus());
    };
  }])
;
