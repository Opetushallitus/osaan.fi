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

angular.module('osaan.arviointi.arviointiui', ['ngRoute', 'ngAnimate'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/arviointi', {
        controller: 'ArviointiController',
        templateUrl: 'template/arviointi/arviointi.html'
      });
  }])

  .controller('ArviointiController', ['$location', '$routeParams', '$scope', '$timeout', 'AmmattitaidonKuvaus', 'Arviointi', 'Tutkinnonosa', 'Tutkinto', function($location, $routeParams, $scope, $timeout, AmmattitaidonKuvaus, Arviointi, Tutkinnonosa, Tutkinto) {
    var tutkinnonosa = $routeParams.osa;
    $scope.tutkinnonosa = tutkinnonosa;
    var tutkintotunnus = Arviointi.valittuTutkintotunnus();
    var peruste = Arviointi.valittuPeruste();
    $scope.arvioidut = [];

    if (tutkinnonosa !== undefined && Arviointi.onkoValittuOsatunnus(tutkinnonosa) && tutkintotunnus !== undefined && peruste !== undefined) {
      Tutkinto.haePerusteella(peruste).then(function(tutkinto) {
        $scope.tutkinto = tutkinto;
      });
      Tutkinnonosa.hae(peruste, tutkintotunnus).then(function(tutkinnonosat) {
        $scope.tutkinnonosat = tutkinnonosat;
        $scope.tutkinnonosatById = _.indexBy(tutkinnonosat, 'osatunnus');
      });
    } else {
      $location.url('/');
    }

    AmmattitaidonKuvaus.haeKohdealueet(tutkinnonosa).then(function(kohdealueet) {
      $scope.kohdealueet = kohdealueet;
      $scope.kohteet = _($scope.kohdealueet).map('kuvaukset').flatten().map('ammattitaidonkuvaus_id').value();
    });

    $scope.arviot = {}; // ammattitaidonkuvaus_id -> {arvio, vapaateksti}
    $scope.$watch('arviot', function(arviot) {
      Arviointi.asetaArviot(tutkinnonosa, arviot);
      $scope.arvioidut = _(arviot).map(function(v, k) { return [k,v]; }).filter(function(x) { return x[1].arvio !== undefined; }).map(function(x) { return parseInt(x[0]); }).value();
    }, true);

    var arviot = Arviointi.haeArviot(tutkinnonosa);
    if (arviot) {
      $scope.arviot = arviot;
    }

    $scope.puuttuukoArvioita = function() {
      return _.difference($scope.kohteet, $scope.arvioidut).length > 0;
    };

    $scope.palaaOsienValintaan = function() {
      $location.url('/osien-valinta?tutkinto=' + Arviointi.valittuTutkintotunnus() + '&peruste=' + Arviointi.valittuPeruste());
    };

    $scope.avaaEdellinenOsa = function() {
      $location.url('/arviointi?osa=' + Arviointi.edellinenOsatunnus(tutkinnonosa));
    };

    $scope.avaaSeuraavaOsa = function() {
      $location.url('/arviointi?osa=' + Arviointi.seuraavaOsatunnus(tutkinnonosa));
    };

    $scope.edellinenTutkinnonosa = function() {
      return Arviointi.edellinenOsatunnus(tutkinnonosa);
    };

    $scope.seuraavaTutkinnonosa = function() {
      return Arviointi.seuraavaOsatunnus(tutkinnonosa);
    };

    $scope.siirryRaporttiin = function() {
      $location.url('/raportti');
    };

    $scope.sivunLoppuun = function() {
      window.scrollTo(0,document.body.scrollHeight);
    };
  }])

  .directive('stickyHeader', [function() {
    return {
      restrict: 'A',
      scope: {
        'top': '='
      },
      link: function(scope, element) {
        var ele = $(element);
        ele.scrollFix({'fixTop': scope.top || 0});
        ele.css('z-index', '1');
      }
    };
  }])
;
