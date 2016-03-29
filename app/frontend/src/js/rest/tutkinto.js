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

angular.module('osaan.rest.tutkinto', [])

  .factory('Tutkinto', ['$http', function($http) {
    return {
      haePerusteella: function(peruste) {
        return $http.get('api/tutkinto/peruste/' + encodeURIComponent(peruste), {cache: true}).then(function(response) {
          return response.data;
        });
      },
      haeEhdoilla: function(kieli, opintoala, nimi, tutkintotyyppi, voimaantulevat) {
        return $http.get('api/tutkinto', {params: {kieli: kieli, opintoala: opintoala, nimi: nimi, tutkintotyyppi: tutkintotyyppi, voimaantulevat: voimaantulevat}}).then(function(response) {
          return response.data;
        });
      }
    };
  }])
;
