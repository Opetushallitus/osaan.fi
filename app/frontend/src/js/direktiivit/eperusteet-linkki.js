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

angular.module('osaan.direktiivit.eperusteet-linkki', [])

  .directive('eperusteetLinkki', [function() {
    return {
      restrict: 'E',
      scope: {
        kieli: '@',
        eperustetunnus: '@',
        eperustetyyppi: '@',
        eperustesivu: '@',
        tutkintotunnus: '@',
        tutkintonimi: '@'
      },
      templateUrl: 'template/direktiivit/eperusteet-linkki.html',
      replace: true,
      link: function(scope) {
        if (scope.kieli === 'fi') {
          scope.opintopolkuUrl = 'https://opintopolku.fi/app/#!/haku/*?page=1&articlePage=1&organisationPage=1&langCleared&itemsPerPage=25&sortCriteria=0&educationCodeFilter=' + encodeURIComponent(scope.tutkintonimi) + '&tab=los';
        } else {
          scope.opintopolkuUrl = 'https://studieinfo.fi/app/#!/haku/*?page=1&articlePage=1&organisationPage=1&langCleared&itemsPerPage=25&sortCriteria=0&educationCodeFilter=' + encodeURIComponent(scope.tutkintonimi) + '&tab=los';
        }
      }
    };
  }])
;
