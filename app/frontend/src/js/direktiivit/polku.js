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

angular.module('osaan.direktiivit.polku', [])

  .directive('polku', [function() {
    return {
      restrict: 'E',
      scope: {
      },
      templateUrl: 'template/direktiivit/polku.html',
      controller: ['$scope', 'Arviointi', function($scope, Arviointi) {
        $scope.$watch(function() {
          return Arviointi.valittuTutkintotunnus();
        }, function(tutkinto) {
          $scope.tutkinto = tutkinto;
        });
        $scope.$watch(function() {
          return Arviointi.valittuPeruste();
        }, function(peruste) {
          $scope.peruste = peruste;
        });
        $scope.$watch(function() {
          return Arviointi.seuraavaOsatunnus();
        }, function(osatunnus) {
          $scope.osatunnus = osatunnus;
        });
        $scope.$watch(function() {
          return Arviointi.onkoArvioita();
        }, function(arvioita) {
          $scope.arvioita = arvioita;
        });
      }]
    };
  }])
;
