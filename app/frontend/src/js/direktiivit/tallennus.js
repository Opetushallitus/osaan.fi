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

angular.module('osaan.direktiivit.tallennus', [])

  .directive('tallennus', [function() {
    return {
      restrict: 'E',
      scope: {
      },
      template: '<div class="tallennus"><a ng-click="tallenna()">Tallenna</a></div>',
      controller: ['$scope', 'Arvio', 'Arviointi', function($scope, Arvio, Arviointi) {
        $scope.tallenna = function() {
          var tila = {};

          tila.tutkintotunnus = Arviointi.valittuTutkintotunnus();
          tila.peruste = Arviointi.valittuPeruste();
          tila.tutkinnonosat = Arviointi.valitutOsatunnukset();
          tila.kohdearviot = {};
          _.forEach(tila.tutkinnonosat, function(tutkinnonosatunnus) {
            tila.kohdearviot[tutkinnonosatunnus] = Arviointi.haeArviot(tutkinnonosatunnus);
          });

          Arvio.tallenna(tila).then(function(data) {
            console.log(data); // TODO
          });
        };
      }]
    };
  }])
;
