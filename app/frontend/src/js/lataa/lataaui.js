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
        template: '',
        label: 'Lataa arvio'
      });
  }])

  .controller('LataaController', ['$location', '$routeParams', 'Arvio', 'Arviointi', function($location, $routeParams, Arvio, Arviointi) {
    Arvio.lataa($routeParams.arviotunnus).then(function(arvio) {
      Arviointi.asetaTutkintoJaPeruste(arvio.tutkintotunnus, arvio.peruste);
      Arviointi.asetaOsatunnukset(arvio.tutkinnonosat);

      _.forEach(arvio.kohdearviot, function(arviot, tutkinnonosatunnus) {
        Arviointi.asetaArviot(tutkinnonosatunnus, arviot);
      });

      $location.url('/osien-valinta?tutkinto=' + arvio.tutkintotunnus + '&peruste=' + arvio.peruste);
    });
  }])
;
