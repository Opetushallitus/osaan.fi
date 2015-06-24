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
        var palkistossaPalkkeja = 1,
          rakoaPalkkienValissa = 10;

        var asetukset = {
          maksimiArvo: 4,
          palkinLeveys: 28,
          palkinMaksimiPituus: 530
        };

        scope.asetukset = asetukset;

        function palkinPituus(asetukset, arvo) {
          return asetukset.palkinMaksimiPituus * arvo / asetukset.maksimiArvo;
        }
        scope.palkinPituus = _.partial(palkinPituus, asetukset);

        scope.paikkaPalkistonSuhteen = function(palkisto, palkki, siirtyma) {
          return (asetukset.palkinLeveys + rakoaPalkkienValissa) * (palkistossaPalkkeja*palkisto + palkki + siirtyma);
        };

        scope.viivastonLeveys = function() {
          return asetukset.palkinMaksimiPituus + 20 * (scope.jakauma.length + 2);
        };

        scope.viivastonKorkeus = function() {
          return (scope.jakauma.length + 1)*(palkistossaPalkkeja*asetukset.palkinLeveys + rakoaPalkkienValissa);
        };

        scope.viivat = [
          {paikka: 0, teksti: '0'},
          {paikka: 0.25, teksti: '1'},
          {paikka: 0.50, teksti: '2'},
          {paikka: 0.75, teksti: '3'},
          {paikka: 1.0, teksti: '4'}
        ];
      }
    };
  }]);
