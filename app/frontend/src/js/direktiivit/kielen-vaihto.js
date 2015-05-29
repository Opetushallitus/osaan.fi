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

angular.module('osaan.direktiivit.kielen-vaihto', [])

  .directive('kielenVaihto', [function() {
    return {
      restrict: 'E',
      template: '<div class="kielen-vaihto pull-right"><a class="btn btn-link" ng-click="asetaKieli(\'fi\')" ng-if="kieli !== \'fi\'" translate="yleiset.kielen_vaihto"></a> <a class="btn btn-link" ng-click="asetaKieli(\'sv\')" ng-if="kieli !== \'sv\'" translate="yleiset.kielen_vaihto"></a></div>',
      controller: ['$scope', '$window', 'kieli', function($scope, $window, kieli) {
        $scope.kieli = kieli;

        $scope.asetaKieli = function(kieli) {
          localStorage.setItem('kieli', kieli);
          $window.location.reload();
        };
      }]
    };
  }])
;
