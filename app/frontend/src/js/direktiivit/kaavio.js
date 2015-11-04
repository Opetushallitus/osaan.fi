// Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
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

angular.module('osaan.direktiivit.kaavio', [])
  .directive('kaavio', [function() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        jakauma: '='
      },
      templateUrl: 'template/direktiivit/kaavio.html',
      link: function(scope) {

        scope.viivat = [
          {paikka: '25%', teksti: '1'},
          {paikka: '50%', teksti: '2'},
          {paikka: '75%', teksti: '3'},
          {paikka: '100%', teksti: '4'}
        ];
      }
    };
  }]);
