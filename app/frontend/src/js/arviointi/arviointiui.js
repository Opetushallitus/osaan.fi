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
      .when('/osien-valinta/arviointi', {
        controller: 'ArviointiController',
        templateUrl: 'template/arviointi/arviointi.html',
        label: 'Arvioi osaaminen'
      });
  }])

  .controller('ArviointiController', ['$location', '$routeParams', '$scope', 'ArvioinninKohteet', 'Arviointi', 'Tutkinto', function($location, $routeParams, $scope, ArvioinninKohteet, Arviointi, Tutkinto) {
    var tutkinnonosa = $routeParams.osa;
    var tutkintotunnus = Arviointi.valittuTutkintotunnus();

    if (tutkintotunnus !== undefined) {
      Tutkinto.hae(tutkintotunnus).then(function(tutkinto) {
        $scope.tutkinto = tutkinto;
      });
    } else {
      $location.url('/');
    }

    ArvioinninKohteet.haeKohdealueet(tutkinnonosa).then(function(kohdealueet) {
      $scope.kohdealueet = kohdealueet;
    });

    $scope.arviot = {}; // arvioinninkohde_id -> {arvio, vapaateksti}
    $scope.$watch('arviot', function(arviot) {
      Arviointi.asetaArviot(tutkinnonosa, arviot);
    }, true);

    var arviot = Arviointi.haeArviot(tutkinnonosa);
    if (arviot) {
      $scope.arviot = arviot;
    }

    $scope.palaaOsienValintaan = function() {
      $location.url('/osien-valinta?tutkinto=' + Arviointi.valittuTutkintotunnus() + '&peruste=' + Arviointi.valittuPeruste());
    };

    $scope.edellinenOsa = function() {
      $location.url('/osien-valinta/arviointi?osa=' + Arviointi.edellinenOsatunnus(tutkinnonosa));
    };

    $scope.seuraavaOsa = function() {
      $location.url('/osien-valinta/arviointi?osa=' + Arviointi.seuraavaOsatunnus(tutkinnonosa));

    };

    $scope.edellinenTutkinnonosa = function() {
      return Arviointi.edellinenOsatunnus(tutkinnonosa);
    };

    $scope.seuraavaTutkinnonosa = function() {
      return Arviointi.seuraavaOsatunnus(tutkinnonosa);
    };
  }])
;
