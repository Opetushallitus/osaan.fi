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

angular.module('osaan.etusivu.etusivuui', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/', {
        controller: 'EtusivuController',
        templateUrl: 'template/etusivu/etusivu.html',
        label: 'Valitse tutkinto'
      });
  }])

  .controller('EtusivuController', ['$scope', 'Arviointi', 'Koulutusala', 'Tutkinto', function($scope, Arviointi, Koulutusala, Tutkinto) {
    $scope.haku = {};
    $scope.koulutusalat = [];
    $scope.tutkinnot = [];

    Koulutusala.hae().then(function(koulutusalat) {
      $scope.koulutusalat = koulutusalat;
    });

    $scope.$watch('haku', function() {
      Tutkinto.haeEhdoilla($scope.haku.opintoala, $scope.haku.tutkinto).then(function(tutkinnot) {
        $scope.tutkinnot = tutkinnot;
      });
    }, true);

    $scope.tyhjennaValitutOsatunnukset = function() {
      Arviointi.asetaOsatunnukset([]);
    };
  }])
;
