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
      template: '<div class="tallennus pull-right" ng-if="tallennusMahdollinen()"><a class="btn btn-primary" ng-click="tallenna()"><span class="glyphicon glyphicon-floppy-disk aria-hidden="true"></span>&nbsp;<span translate="yleiset.tallenna"></span></a></div>',
      controller: ['$modal', '$scope', 'Arvio', 'Arviointi', function($modal, $scope, Arvio, Arviointi) {
        $scope.tallennusMahdollinen = function() {
          return Arviointi.onkoArvioita();
        };

        $scope.tallenna = function() {
          var tila = {};

          tila.tutkintotunnus = Arviointi.valittuTutkintotunnus();
          tila.peruste = parseInt(Arviointi.valittuPeruste());
          tila.tutkinnonosat = Arviointi.valitutOsatunnukset();
          tila.kohdearviot = {};
          _.forEach(tila.tutkinnonosat, function(tutkinnonosatunnus) {
            tila.kohdearviot[tutkinnonosatunnus] = Arviointi.haeArviot(tutkinnonosatunnus);
          });

          Arvio.tallenna(tila).then(function(tunnus) {
            $modal.open({
              templateUrl: 'template/direktiivit/tallennus.html',
              controller: 'TallennusModalController',
              resolve: {
                tunnus: function() { return tunnus; }
              }
            }).result.then(function() {
              Arviointi.asetaLadatuksi();
            }, function() {
              Arviointi.asetaLadatuksi();
            });
          });
        };
      }]
    };
  }])

  .controller('TallennusModalController', ['$modalInstance', '$scope', 'tunnus', function($modalInstance, $scope, tunnus) {
    $scope.linkki = document.location.href.split('#')[0] + '#/lataa/' + tunnus;

    $scope.close = $modalInstance.close;
  }])

  .directive('clickToSelect', function() {
    return {
      restrict: 'A',
      link: function(scope, element) {
        element.on('click', function() {
          this.select();
        });
      }
    };
  })
;
