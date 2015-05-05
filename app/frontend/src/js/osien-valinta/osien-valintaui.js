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

  .controller('OsienValintaController', ['$location', '$routeParams', '$scope', 'Tutkinnonosa', function($location, $routeParams, $scope, Tutkinnonosa) {
    var peruste = $routeParams.peruste;
    var tutkintotunnus = $routeParams.tutkinto;

    $scope.valinnat = {};

    $scope.eteenpain = function() {
      var valinnat = _($scope.valinnat)
        .map(function(valittu, osatunnus) { return [osatunnus, valittu]; })
        .filter(function(x) { return x[1]; })
        .map(function(x) { return x[0]; })
        .value();

      sessionStorage.setItem('tutkinnonosat', JSON.stringify(valinnat));
      $location.url('/osien-valinta/arviointi');
    };

    Tutkinnonosa.hae(peruste, tutkintotunnus).then(function(tutkinnonosat) {
      $scope.tutkinnonosat = tutkinnonosat;
    });
  }])
;
