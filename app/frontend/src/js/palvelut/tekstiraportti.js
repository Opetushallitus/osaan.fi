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

angular.module('osaan.palvelut.tekstiraportti', [])

  .factory('TekstiRaportti', ['$filter', '$translate', 'Arviointi', function($filter, $translate, Arviointi) {
    return {
      luoRaportti: function(tutkinto, tutkinnonosat, kohdealueet) {
        var lokalisoiKentta = $filter('lokalisoiKentta');
        var t = '';

        // Otsikko
        t += lokalisoiKentta(tutkinto, 'koulutusala_nimi');
        t += ' -> ';
        t += lokalisoiKentta(tutkinto, 'opintoala_nimi');
        t += ' -> ';
        t += lokalisoiKentta(tutkinto, 'nimi');
        t += '\n\n\n';

        t += 'Päiväys: ';
        t += $filter('date')(new Date(), 'dd.MM.yyyy');
        t += '\n\n\n';

        // Numeroiden selitykset
        t += $translate.instant('raportti.valintojen_selitykset');
        for (var i = 1; i <= 4; i++) {
          t += ' ' + String(i) + ' = ' + $translate.instant('arviointi.asteikko' + i);
        }
        t += ' ? = ';
        t += $translate.instant('arviointi.eos');
        t += '\n\n';

        t += 'Arvio   Tutkinnon osa -> kohdealue -> ammattitaito\n';
        t += '=====   ==========================================\n\n\n';

        _.forEach(tutkinnonosat, function(tutkinnonosa) {
          var arviot = Arviointi.haeArviot(tutkinnonosa.osatunnus);

          // TODO arvio tutkinnonosalle
          t += '___     ' + lokalisoiKentta(tutkinnonosa, 'nimi') + '\n';
          _.forEach(kohdealueet[tutkinnonosa.osatunnus], function(kohdealue) {
            // TODO arvio kohdealueelle
            t += '___        ' + lokalisoiKentta(kohdealue, 'nimi') + '\n';

            _.forEach(kohdealue.kuvaukset, function(kuvaus) {
              var arvio = arviot[kuvaus.ammattitaidonkuvaus_id];
              var num = '-  ';
              if (arvio) {
                num = arvio.arvio ? arvio.arvio : '?  ';
              }
              t += num + '              ' + lokalisoiKentta(kuvaus, 'nimi') + '\n';
              if (arvio && arvio.vapaateksti) {
                t += '               KOMMENTTI: ' + arvio.vapaateksti + '\n';
              }
            });
          });
        });

        return t;
      }
    };
  }])
;
