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

  .factory('TekstiRaportti', ['$filter', '$translate', function($filter, $translate) {
    return {
      luoRaportti: function(raportti) {
        var lokalisoiKentta = $filter('lokalisoiKentta');
        var number = $filter('number');
        var t = '';

        // Otsikko
        t += lokalisoiKentta(raportti, 'koulutusala_nimi');
        t += ' -> ';
        t += lokalisoiKentta(raportti, 'opintoala_nimi');
        t += ' -> ';
        t += lokalisoiKentta(raportti, 'tutkinto_nimi');
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
        t += '=====   ==========================================\n\n';

        t += number(raportti.keskiarvo, 2) + '    ' + lokalisoiKentta(raportti, 'nimi') + '\n';

        _.forEach(raportti.tutkinnonosat, function(tutkinnonosa) {
          t += number(tutkinnonosa.keskiarvo, 2) + '    ' + lokalisoiKentta(tutkinnonosa, 'nimi') + '\n';
          _.forEach(tutkinnonosa.kohdealueet, function(kohdealue) {
            t += number(kohdealue.keskiarvo, 2) + '       ' + lokalisoiKentta(kohdealue, 'nimi') + '\n';

            _.forEach(kohdealue.kuvaukset, function(kuvaus) {
              var num = '-  ';
              if (kuvaus.arvio) {
                num = kuvaus.arvio.arvio ? kuvaus.arvio.arvio : '?';
              }
              t += num + '             ' + lokalisoiKentta(kuvaus, 'nimi') + '\n';
              if (kuvaus.arvio && kuvaus.arvio.vapaateksti) {
                t += '              KOMMENTTI: ' + kuvaus.arvio.vapaateksti + '\n';
              }
            });
          });
        });

        return t;
      }
    };
  }])
;
