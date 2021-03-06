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

angular.module('osaan.lataa.lataaui', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/lataa/:arviotunnus', {
        controller: 'LataaController',
        template: '<div ng-if="virhe" class="alert alert-danger"><span translate="lataa.virhe"></span> <a href="#/" translate="lataa.palaa_etusivulle"></a></div>'
      });
  }])

  .controller('LataaController', ['$filter', '$location', '$routeParams', '$scope', '$translate', '$window', 'Arvio', 'Arviointi', 'Huomautus', 'kieli', function($filter, $location, $routeParams, $scope, $translate, $window, Arvio, Arviointi, Huomautus, kieli) {
    // Kielen vaihto vaatii reloadin kieli-factorystä johtuen :(
    if ($routeParams.kieli !== undefined && _.indexOf(['fi', 'sv'], $routeParams.kieli) !== -1 && $routeParams.kieli !== kieli) {
      localStorage.setItem('kieli', $routeParams.kieli);
      $window.location.reload();
      return;
    }

    Arvio.lataa($routeParams.arviotunnus).then(function(arvio) {
      Arviointi.lataa(arvio);

      var pvm = $filter('date')(arvio.luotuaika, 'dd.MM.yyyy HH:mm:ss');
      Huomautus.naytaHuomautus($translate.instant('osien-valinta.arvio_ladattu_otsikko'), $translate.instant('osien-valinta.arvio_ladattu', {luotuaika: pvm}));

      $location.url('/osien-valinta?tutkinto=' + arvio.tutkintotunnus + '&peruste=' + arvio.peruste);
    }, function() {
      $scope.virhe = true;
    });
  }])
;
