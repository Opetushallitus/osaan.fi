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

angular.module('osaan.raportti.raporttiui', ['ngRoute'])

  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/raportti', {
        controller: 'RaporttiController',
        templateUrl: 'template/raportti/raportti.html',
        label: 'Raportti'
      });
  }])

  .controller('RaporttiController', ['$location', '$routeParams', '$scope', 'ArvioinninKohde', 'Arviointi', 'Tutkinnonosa', 'Tutkinto', function($location, $routeParams, $scope, ArvioinninKohde, Arviointi, Tutkinnonosa, Tutkinto) {

    Tutkinnonosa.hae(Arviointi.valittuPeruste(), Arviointi.valittuTutkintotunnus())
      .then(function(tutkinnonosat) {
        $scope.tutkinnonosat = _(tutkinnonosat).groupBy('osatunnus').pick(Arviointi.valitutOsatunnukset()).values().flatten().value();
      });

    ArvioinninKohde.haeKohdealueetTutkinnonosille(Arviointi.valitutOsatunnukset())
      .then(function(kohdealueet) {
        $scope.kohdealueet = kohdealueet;
      });

    $scope.arviot = _.zipObject(
      _.map(Arviointi.valitutOsatunnukset(),
        function(osatunnus) {return [osatunnus, Arviointi.haeArviot(osatunnus)];}));

    $scope.palaaArviointiin = function() {
      $location.url('/osien-valinta/arviointi?osa=' + Arviointi.seuraavaOsatunnus());
    };
  }])
;
