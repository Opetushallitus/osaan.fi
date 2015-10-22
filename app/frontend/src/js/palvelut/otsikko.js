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

angular.module('osaan.palvelut.otsikko', [])

  .factory('Otsikko', ['$rootScope', '$translate', function($rootScope, $translate) {
    var sivunAvain = undefined;
    var alaotsikko = null;
    var otsikko = 'osaan.fi';

    var paivitaOtsikko = function() {
      otsikko = 'osaan.fi';
      if (sivunAvain !== undefined) {
        otsikko += ' - ' + $translate.instant(sivunAvain);
      }
      if (alaotsikko !== null) {
        otsikko += ' - ' + alaotsikko;
      }
    };

    $rootScope.$on('$routeChangeSuccess', function(event, current) {
      sivunAvain = null;
      alaotsikko = null;
      if (current.$$route.title !== undefined) {
        sivunAvain = current.$$route.title;
      }
      paivitaOtsikko();
    });

    return {
      asetaAlaOtsikko: function(alaOtsikko_) {
        alaotsikko = alaOtsikko_;
        paivitaOtsikko();
      },
      haeOtsikko: function() {
        return otsikko;
      }
    }
  }])

  .directive('titleInjection', ['Otsikko', function(Otsikko) {
    return {
      restrict: 'A',
      scope: true,
      link: function(scope) {
        scope.$watch(function() {
          return Otsikko.haeOtsikko();
        }, function(otsikko) {
          scope.title = otsikko;
        });
      }
    };
  }])
;
