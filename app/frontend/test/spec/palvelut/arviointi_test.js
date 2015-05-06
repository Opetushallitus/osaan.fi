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

describe('Arviointi', function() {
  beforeEach(module('osaan.palvelut.arviointi'));

  var Arviointi;

  beforeEach(inject(function(_Arviointi_) {
    Arviointi = _Arviointi_;
  }));

  beforeEach(function() {
    Arviointi.asetaTutkintoJaPeruste('324601', '41/011/2005');
    Arviointi.asetaOsatunnukset(['100001', '100002']);
  });

  it('Valinnat saa luettua', function() {
    expect(Arviointi.valittuTutkintotunnus()).toEqual('324601');
    expect(Arviointi.valittuPeruste()).toEqual('41/011/2005');
    expect(Arviointi.valitutOsatunnukset()).toEqual(['100001', '100002']);
  });

  it('Edellinen osatunnus', function() {
    expect(Arviointi.edellinenOsatunnus('100002')).toEqual('100001');
    expect(Arviointi.edellinenOsatunnus('100001')).toEqual(undefined);
  });

  it('Seuraava osatunnus', function() {
    expect(Arviointi.seuraavaOsatunnus()).toEqual('100001');
    expect(Arviointi.seuraavaOsatunnus('100001')).toEqual('100002');
  });
});
