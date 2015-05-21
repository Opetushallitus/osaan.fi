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

angular.module('osaan.rest.ammattitaidonkuvaus', [])

  .factory('AmmattitaidonKuvaus', ['$http', function($http) {
    function haeKohdealueetTutkinnonosille(tutkinnonosatunnukset) {
      return $http.get('api/arvioinninkohde/alueet', {cache: true, params: {tutkinnonosatunnus: tutkinnonosatunnukset}})
        .then(function(response) {
          return response.data;
        });
    }

    return {
      haeKohdealueet: function(tutkinnonosatunnus) {
        return haeKohdealueetTutkinnonosille(tutkinnonosatunnus)
          .then(function(kohdealueet) {
            return kohdealueet[tutkinnonosatunnus];
          });
      },
      haeKohdealueetTutkinnonosille: haeKohdealueetTutkinnonosille
    };
  }])
;
