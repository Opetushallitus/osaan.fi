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

angular.module('osaan.direktiivit.ohje', ['osaan.palvelut.ohje'])

  .directive('ohje', [function() {
    return {
      restrict: 'E',
      scope: {
        tunniste: '@'
      },
      templateUrl: 'template/direktiivit/ohje.html',
      replace: true,
      controller: ['$filter', '$scope', 'Ohje', function($filter, $scope, Ohje) {
        $scope.ohje = [];

        $scope.naytaOhje = function() {
          $scope.nayta = true;
        };

        $scope.piilotaOhje = function() {
          $scope.nayta = false;
        };

        Ohje.hae($scope.tunniste).then(function(ohje) {
          var lokalisoitu = $filter('lokalisoiKentta')(ohje, 'teksti');
          var pilkottu = lokalisoitu ? lokalisoitu.split(/\n/g) : [];
          $scope.ohje = pilkottu;
        });
      }]
    }
  }])
;
