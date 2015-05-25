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

angular.module('osaan.palvelut.varmistus', [])
  .factory('varmistus', ['$modal', '$q', function($modal, $q) {
    return {
      varmista: function(otsikko, teksti, vahvistusnappi) {
        var deferred = $q.defer();

        var modalInstance = $modal.open({
          templateUrl: 'template/palvelut/varmistus.html',
          controller: 'VarmistusModalController',
          resolve: {
            tekstit: function() {
              return {
                otsikko: otsikko,
                teksti: teksti,
                vahvistusnappi: vahvistusnappi
              };
            }
          }
        });

        modalInstance.result.then(function() {
          deferred.resolve();
        }, function() {
          deferred.reject();
        });

        return deferred.promise;
      }
    };
  }])

  .controller('VarmistusModalController', ['$modalInstance', '$scope', 'tekstit', function($modalInstance, $scope, tekstit) {
    $scope.otsikko = tekstit.otsikko;
    $scope.teksti = tekstit.teksti;
    $scope.vahvistusnappi = tekstit.vahvistusnappi;

    $scope.ok = $modalInstance.close;
    $scope.cancel = $modalInstance.dismiss;
  }])
;