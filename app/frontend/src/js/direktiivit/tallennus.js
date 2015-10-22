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

angular.module('osaan.direktiivit.tallennus', ['osaan.palvelut.tallennus'])

  .directive('tallennus', [function() {
    return {
      restrict: 'E',
      scope: {
      },
      template: '<button class="btn btn-primary pull-right" ng-click="tallenna()" ng-if="tallennusMahdollinen()"><span class="glyphicon glyphicon-floppy-disk aria-hidden="true"></span>&nbsp;<span translate="yleiset.tallenna"></span></button>',
      controller: ['$uibModal', '$scope', 'Arviointi', 'Tallennus', function($uibModal, $scope, Arviointi, Tallennus) {
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

          Tallennus.tallenna(tila).then(function(tunnus) {
            $uibModal.open({
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

  .controller('TallennusModalController', ['$modalInstance', '$scope', 'kieli', 'tunnus', function($modalInstance, $scope, kieli, tunnus) {
    $scope.linkki = document.location.href.split('#')[0] + '#/lataa/' + tunnus + '?kieli=' + kieli;
    $scope.linkki_mailto = encodeURIComponent($scope.linkki);

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
