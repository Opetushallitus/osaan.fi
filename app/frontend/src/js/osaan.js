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

angular.module('osaan.fi', [
  'ngAnimate',
  'ngRoute',
  'taiPlaceholder',
  'ui.bootstrap',

  'yhteiset.palvelut.lokalisointi',

  'osaan.arviointi.arviointiui',
  'osaan.etusivu.etusivuui',
  'osaan.direktiivit.kielen-vaihto',
  'osaan.direktiivit.polku',
  'osaan.direktiivit.ohje',
  'osaan.direktiivit.tallennus',
  'osaan.direktiivit.eperusteet-linkki',
  'osaan.lataa.lataaui',
  'osaan.osien-valinta.osien-valintaui',
  'osaan.palvelut.arviointi',
  'osaan.palvelut.varmistus',
  'osaan.raportti.raporttiui',
  'osaan.rest.ammattitaidonkuvaus',
  'osaan.rest.arvio',
  'osaan.rest.koulutusala',
  'osaan.rest.tutkinnonosa',
  'osaan.rest.tutkinto',
  'osaan.tekstit'
])
;
