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

angular.module('osaan.palvelut.poistumisvaroitus', [])

  // Poistumisvaroitus
  .factory('Poistumisvaroitus', ['$timeout', 'Arviointi', function($timeout, Arviointi) {
    var estetty = false;
    var confirmBeforeUnload = function(e) {
      if (estetty || Arviointi.onkoLadattu() || !Arviointi.onkoArvioita()) {
        return;
      }

      var confirmationMessage = 'Haluatko varmasti poistua tallentamatta?';

      (e || window.event).returnValue = confirmationMessage; // Gecko + IE
      return confirmationMessage; // Webkit, Safari, Chrome etc.
    };

    window.addEventListener('beforeunload', confirmBeforeUnload, false);

    return {
      // Workaround Chromelle, joka triggeröi beforeunload-eventin vaikka vastauksena tulee Content-Disposition: attachment
      estaPoistumisvaroitus: function() {
        estetty = true;

        $timeout(function() {
          estetty = false;
        }, 1000);
      }
    }
  }])
;
