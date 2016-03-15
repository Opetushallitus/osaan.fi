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

angular.module('osaan.direktiivit.tutkinnonosa-tilastot', [])

  .directive('tutkinnonosaTilastot', [function() {
    return {
      restrict: 'E',
      scope: {
        kaikkiTutkinnonosat: '=',
        otsikko: '@',
        tutkinnonosat: '=',
        tyyppi: '@'
      },
      templateUrl: 'template/direktiivit/tutkinnonosa-tilastot.html',
      controller: ['$filter', '$scope', function($filter, $scope) {
        var korjaaTyypit = function(kaikkiTutkinnonosat, tutkinnonosat) {
          var tyypit = {};
          _.forEach(kaikkiTutkinnonosat, function(osa) {
            tyypit[osa.osatunnus] = osa.tyyppi;
          });
          return _.map(tutkinnonosat, function(osa) {
            osa.tyyppi = tyypit[osa.osatunnus];
            return osa;
          });
        };

        var suodataTutkinnonosat = function(kaikkiTutkinnonosat, tutkinnonosat) {
          if(kaikkiTutkinnonosat !== undefined && tutkinnonosat !== undefined) {
            $scope.suodatetutTutkinnonosat = $filter('filter')(korjaaTyypit(kaikkiTutkinnonosat, tutkinnonosat), {tyyppi: $scope.tyyppi});
          }
        };

        $scope.$watch('kaikkiTutkinnonosat', function(kaikkiTutkinnonosat) {
          if (kaikkiTutkinnonosat !== undefined) {
            $scope.tutkinnonosiaTyypilla = $filter('filter')(kaikkiTutkinnonosat, {tyyppi: $scope.tyyppi}).length;
            suodataTutkinnonosat(kaikkiTutkinnonosat, $scope.tutkinnonosat);
          }
        });

        $scope.$watch('tutkinnonosat', function(tutkinnonosat) {
          if (tutkinnonosat !== undefined) {
            suodataTutkinnonosat($scope.kaikkiTutkinnonosat, tutkinnonosat);
          }
        });
      }]
    };
  }])
;
