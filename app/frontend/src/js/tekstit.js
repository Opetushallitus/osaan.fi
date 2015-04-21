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

angular.module('osaan.tekstit', ['pascalprecht.translate'])
  .constant('tekstit', {
    fi: {
      'etusivu.teksti': 'Tällä työkalulla voit arvioida osaamistasi sinua kiinnostavassa ammatissa (Näyttötutkinnossa). Voit valita joko osaamisvaatimusten selaamisen tai osaamisen arvioinnin. Selaustoiminnossa voit selailla eri tutkintojen osaamisvaatimuksia. Arvioin osaamistani -toiminnossa voit vastata valitsemasi tutkinnon osaamisvaatimuksiin ja lopuksi saat tuloksena listan vastauksistasi ja graafiset kuvaajat osaamisvaatimusten keskiarvoista. Voit myös lähettää raportin pdf-tiedostona haluamiisi sähköpostiosoitteisiin.',
      'etusivu.otsikko': 'Osaamisen tunnistaminen',
      'footer.ota_yhteytta': 'osaan.fi käyttöä koskevan palautteen voi lähettää sähköpostilla osoitteeseen',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Palvelun tarjoaa Opetushallitus',
      'footer.rekisteriseloste': 'osaan.fi rekisteriselosteen voit lukea osoitteesta:',
      'footer.rekisteriseloste_url': 'http://osaan.fi/Rekisteriseloste%20Osaan%20fi.pdf'
    },
    sv: {
      'etusivu.teksti': 'Tällä työkalulla voit arvioida osaamistasi sinua kiinnostavassa ammatissa (Näyttötutkinnossa). Voit valita joko osaamisvaatimusten selaamisen tai osaamisen arvioinnin. Selaustoiminnossa voit selailla eri tutkintojen osaamisvaatimuksia. Arvioin osaamistani -toiminnossa voit vastata valitsemasi tutkinnon osaamisvaatimuksiin ja lopuksi saat tuloksena listan vastauksistasi ja graafiset kuvaajat osaamisvaatimusten keskiarvoista. Voit myös lähettää raportin pdf-tiedostona haluamiisi sähköpostiosoitteisiin. (sv)',
      'etusivu.otsikko': 'Osaamisen tunnistaminen (sv)',
      'footer.ota_yhteytta': 'osaan.fi käyttöä koskevan palautteen voi lähettää sähköpostilla osoitteeseen (sv)',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Palvelun tarjoaa Opetushallitus (sv)',
      'footer.rekisteriseloste': 'osaan.fi rekisteriselosteen voit lukea osoitteesta:',
      'footer.rekisteriseloste_url': 'http://osaan.fi/Rekisteriseloste%20Osaan%20fi.pdf'
    }
  })

  .config(['$translateProvider', 'tekstit', function($translateProvider, tekstit) {
    $translateProvider.translations('fi', tekstit.fi);
    $translateProvider.translations('sv', tekstit.sv);

    $translateProvider.preferredLanguage('fi');
  }])
;
