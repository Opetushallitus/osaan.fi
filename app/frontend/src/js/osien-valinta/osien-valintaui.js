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

angular.module('osaan.osien-valinta.osien-valintaui', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/osien-valinta', {
        controller: 'OsienValintaController',
        templateUrl: 'template/osien-valinta/osien-valinta.html',
        label: 'Valitse tutkinnon osat'
      });
  }])

  .controller('OsienValintaController', ['$location', '$routeParams', '$scope', 'Arviointi', 'Tutkinnonosa', 'Tutkinto', function($location, $routeParams, $scope, Arviointi, Tutkinnonosa, Tutkinto) {
    var peruste = $routeParams.peruste;
    var tutkintotunnus = $routeParams.tutkinto;

    Arviointi.asetaTutkintoJaPeruste(tutkintotunnus, peruste);

    var valitutOsatunnukset = function() {
      return _($scope.valinnat)
        .map(function(valittu, osatunnus) { return [osatunnus, valittu]; })
        .filter(function(x) { return x[1]; })
        .map(function(x) { return x[0]; })
        .value();
    };

    $scope.valinnat = {};
    $scope.$watch('valinnat', function() {
      Arviointi.asetaOsatunnukset(valitutOsatunnukset());
    }, true);

    // Kun sivulle palataan uudestaan, palauta valinnat
    _.forEach(Arviointi.valitutOsatunnukset(), function(osatunnus) {
      $scope.valinnat[osatunnus] = true;
    });

    Tutkinto.hae(tutkintotunnus).then(function(tutkinto) {
      $scope.tutkinto = tutkinto;
    });

    $scope.seuraavaTutkinnonosa = function() {
      return Arviointi.seuraavaOsatunnus();
    };

    $scope.eteenpain = function() {
      $location.url('/osien-valinta/arviointi?osa=' + Arviointi.seuraavaOsatunnus());
    };

    Tutkinnonosa.hae(peruste, tutkintotunnus).then(function(tutkinnonosat) {
      $scope.tutkinnonosat = tutkinnonosat;
    });
  }])
;
