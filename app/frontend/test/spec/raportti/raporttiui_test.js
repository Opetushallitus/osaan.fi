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

describe('osaan.raportti.raporttiui:', function() {
  beforeEach(module('osaan.raportti.raporttiui'));

  describe('RaporttiApurit:', function() {
    var RaporttiApurit;

    beforeEach(inject(function(_RaporttiApurit_) {
      RaporttiApurit = _RaporttiApurit_;
    }));

    describe('arvioidenKeskiarvo', function() {
      it('pitäisi laskea arvioiden keskiarvo', function () {
        expect(RaporttiApurit.arvioidenKeskiarvo({'a': {arvio: 2}, 'b': {arvio: 4}})). toEqual(3);
      });

      it('pitäisi palauttaa nolla tyhjälle', function () {
        expect(RaporttiApurit.arvioidenKeskiarvo({})). toEqual(0);
      });
    });

    describe('valitseTutkinnonOsat:', function() {
      it('pitäisi valita tutkinnon osat osatunnuksella', function () {
        expect(RaporttiApurit.valitseTutkinnonOsat([{osatunnus: 1}, {osatunnus: 2}], [2]))
          .toEqual([{osatunnus: 2}]);
      });

      it('pitäisi palauttaa kaikki tutkinnon osan kentät', function () {
        expect(RaporttiApurit.valitseTutkinnonOsat([{osatunnus: 1, kentta: 'arvo'}], [1]))
          .toEqual([{osatunnus: 1, kentta: 'arvo'}]);
      });
    });

    describe('liitaTutkinnonOsiinArviot:', function() {
      it('pitäisi liittää osiin arviot', function () {
        function haeArviot(osatunnus) {
          if (osatunnus === 1) {
            return {i: 'arviot1'};
          } else if (osatunnus === 2) {
            return {i: 'arviot2'};
          }
        }
        expect(RaporttiApurit.liitaTutkinnonOsiinArviot([1, 2], haeArviot))
          .toEqual({
            1: {i: 'arviot1'},
            2: {i: 'arviot2'}
          });
      });
    });

    describe('muodostaJakauma:', function() {
      it('pitäisi muodostaa jakauma kaaviota varten', function () {
        expect(RaporttiApurit.muodostaJakauma([{nimi_fi: 'fi', nimi_sv: 'sv', osatunnus: 1, pakollinen: true}], {1: 3.5}))
          .toEqual([{arvo: 3.5, nimi_fi: 'fi', nimi_sv: 'sv', vari: 0}]);
      });

      it('pitäisi vaihtaa valinnaisen väri', function () {
        expect(RaporttiApurit.muodostaJakauma([{nimi_fi: 'fi', nimi_sv: 'sv', osatunnus: 1, pakollinen: false}], {1: 3.5}))
          .toEqual([{arvo: 3.5, nimi_fi: 'fi', nimi_sv: 'sv', vari: 1}]);
      });
    });
  });
});
