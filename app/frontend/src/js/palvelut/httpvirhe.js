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

angular.module('osaan.palvelut.httpvirhe', [])
  .factory('Httpvirhe', [function() {
    var service = {};
    var virhetila = false;
    var yhteyskatko = 0;

    service.asetaVirhetila = function(virhe) {
      virhetila = virhe;
    };

    service.asetaYhteyskatko = function(katko) {
      if (katko) {
        yhteyskatko++;
      } else {
        yhteyskatko--;
      }
    };

    service.onkoVirhetila = function() {
      return virhetila;
    };

    service.onkoYhteyskatko = function() {
      return yhteyskatko > 0;
    };

    return service;
  }])

  .factory('httpResponseErrorInterceptor', ['$injector', '$q', '$timeout', 'Httpvirhe', function($injector, $q, $timeout, Httpvirhe) {
    return {
      'responseError': function(response) {
        if (response.status === 0) {
          Httpvirhe.asetaYhteyskatko(true);
          return $timeout(function() {
            var $http = $injector.get('$http');
            Httpvirhe.asetaYhteyskatko(false);
            return $http(response.config);
          }, 15000);
        }
        Httpvirhe.asetaVirhetila(true);
        return $q.reject(response);
      }
    };
  }])

  .config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('httpResponseErrorInterceptor');
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
    $httpProvider.defaults.headers.common["Caller-Id"] = "1.2.246.562.10.00000000001.osaan";
    $httpProvider.defaults.xsrfHeaderName = "CSRF";
    $httpProvider.defaults.xsrfCookieName = "CSRF";
  }])
;
