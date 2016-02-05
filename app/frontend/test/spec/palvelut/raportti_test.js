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

describe('Raportti', function() {
  beforeEach(module('osaan.palvelut.arviointi'));
  beforeEach(module('osaan.palvelut.raportti'));

  var Arviointi;
  var Raportti;

  beforeEach(inject(function(_Arviointi_, _Raportti_) {
    Arviointi = _Arviointi_;
    Raportti = _Raportti_;
  }));

  beforeEach(function() {
    Arviointi.asetaTutkintoJaPeruste('324601', '41/011/2005');
    Arviointi.asetaOsat({undefined: {'100001': true, '100002': true}});
    Arviointi.tyhjennaArviot();
    Arviointi.asetaArviot('100001', {'-1': {arvio: 1}, '-2': {arvio: 3}, '-3': {vapaateksti: 'testi'}});
  });

  it('Muodostaa raportin oikein', function() {
    // Tutkinto.haePerusteella(Arviointi.valittuPeruste())
    var tutkinto = {
      nimi_fi: 'Audiovisuaalisen viestinnän perustutkinto',
      peruste_eperustetunnus: 611,
      nimi_sv: 'Audiovisuaalisen viestinnän perustutkinto (sv)',
      opintoala_nimi_sv: 'Mediekultur och informationsvetenskaper',
      peruste_id: -1,
      opintoala_nimi_fi: 'Viestintä ja informaatiotieteet',
      koulutusala_nimi_sv: 'Kultur',
      peruste_diaarinumero: '41/011/2005',
      koulutusala_nimi_fi: 'Kulttuuriala',
      tutkintotunnus: '324601',
      peruste_tyyppi: 'ops'
    };

    // Tutkinnonosa.hae(Arviointi.valittuPeruste(), Arviointi.valittuTutkintotunnus())
    var tutkinnonosat = [
      {pakollinen: true, osatunnus: '100001', nimi_sv: 'Audiovisuaalinen tuotanto (sv)', nimi_fi: 'Audiovisuaalinen tuotanto'},
      {pakollinen: true, osatunnus: '100002', nimi_sv: 'Video- ja elokuvatuotanto (sv)', nimi_fi: 'Video- ja elokuvatuotanto'}
    ];

    // AmmattitaidonKuvaus.haeKohdealueetTutkinnonosille(Arviointi.valitutOsatunnukset())
    var kohdealueet = {
      '100001': [
        {
          kuvaukset: [
            {nimi_sv: 'Förproduktion', nimi_fi: 'Esituotanto', ammattitaidonkuvaus_id:-1},
            {nimi_sv: 'Produktion', nimi_fi: 'Tuotanto', ammattitaidonkuvaus_id:-2},
            {nimi_sv: 'Efterproduktion', nimi_fi: 'Jälkituotanto', ammattitaidonkuvaus_id:-3}
          ],
          nimi_sv: 'Behärskande av arbetsprocessen',
          nimi_fi: '1. Työprosessin hallinta',
          arvioinninkohdealue_id: -1
        },
        {
          kuvaukset: [
            {nimi_sv: 'Planering av det egna arbetet', nimi_fi: 'Oman työn suunnittelu', ammattitaidonkuvaus_id: -4},
            {nimi_sv: 'Produktion av material', nimi_fi: 'Aineiston tuottaminen', ammattitaidonkuvaus_id: -5}
          ],
          nimi_sv: 'Behärskande av arbetsmetoder, -redskap och material',
          nimi_fi: '2. Työmenetelmien, -välineiden ja materiaalin hallinta',
          arvioinninkohdealue_id: -2
        }
      ],
      '100002': []
    };

    var raportti = Raportti.luoRaportti(tutkinto, tutkinnonosat, kohdealueet);

    // Tarkista rakenne
    expect(raportti.tutkinnonosat).not.toEqual(undefined);
    expect(raportti.tutkinnonosat[0].kohdealueet).not.toEqual(undefined);
    expect(raportti.tutkinnonosat[0].kohdealueet[0].kuvaukset).not.toEqual(undefined);

    // Vastausprosentit
    expect(raportti.tutkinnonosat[0].arvioita).toEqual(2);
    expect(raportti.tutkinnonosat[0].arvioitavia).toEqual(5);
    expect(raportti.tutkinnonosat[0].arvioituna).toEqual(40);

    // Keskiarvojen laskenta
    expect(raportti.keskiarvo).toEqual(2);
    expect(raportti.tutkinnonosat[0].keskiarvo).toEqual(2);
    expect(raportti.tutkinnonosat[0].kohdealueet[0].keskiarvo).toEqual(2);
    expect(raportti.tutkinnonosat[0].kohdealueet[0].kuvaukset[0].arvio.arvio).toEqual(1);
  });
});
