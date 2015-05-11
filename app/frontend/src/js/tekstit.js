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
      'arvointi.asteikko1' : 'En osaa',
      'arvointi.asteikko2' : 'Osaan hiukan',
      'arvointi.asteikko3' : 'Osaan melko hyvin',
      'arvointi.asteikko4' : 'Osaan hyvin',
      'etusivu.opintoala': 'Opintoala',
      'etusivu.teksti': 'Tällä työkalulla voit arvioida osaamistasi sinua kiinnostavassa ammatissa (Näyttötutkinnossa). Palvelussa voit vastata valitsemasi tutkinnon osaamisvaatimuksiin ja lopuksi saat tuloksena listan vastauksistasi ja graafiset kuvaajat osaamisvaatimusten keskiarvoista. Voit myös lähettää raportin pdf-tiedostona haluamiisi sähköpostiosoitteisiin.',
      'etusivu.tutkinnon_nimi': 'Tutkinnon nimi',
      'etusivu.tutkinnon_nimi_placeholder': 'Kirjoita tutkinnon nimi tai nimen osa',
      'etusivu.otsikko': 'Osaamisen tunnistaminen',
      'footer.ota_yhteytta': 'osaan.fi käyttöä koskevan palautteen voi lähettää sähköpostilla osoitteeseen',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Palvelun tarjoaa Opetushallitus',
      'footer.rekisteriseloste': 'osaan.fi rekisteriselosteen voit lukea osoitteesta:',
      'footer.rekisteriseloste_url': 'http://osaan.fi/Rekisteriseloste%20Osaan%20fi.pdf',
      'osien-valinta.eteenpain': 'Eteenpäin',
      'osien-valinta.pakolliset_osat': 'Pakolliset osat',
      'osien-valinta.valinnaiset_osat': 'Valinnaiset osat',
      'raportti.arvio': 'Arvio',
      'raportti.arvioinnin_kohde': 'Arvioinnin kohde',
      'raportti.kommentti': 'Kommentti',
      'raportti.otsikko': 'Raportti',
      'raportti.paivays': 'Päiväys',
      'raportti.palaa_arviointiin': 'Arvioi osaaminen'
    },
    sv: {
      'arvointi.asteikko1' : 'En osaa (sv)',
      'arvointi.asteikko2' : 'Osaan hiukan (sv)',
      'arvointi.asteikko3' : 'Osaan melko hyvin (sv)',
      'arvointi.asteikko4' : 'Osaan hyvin (sv)',
      'etusivu.opintoala': 'Opintoala (sv)',
      'etusivu.teksti': 'Tällä työkalulla voit arvioida osaamistasi sinua kiinnostavassa ammatissa (Näyttötutkinnossa). Palvelussa voit vastata valitsemasi tutkinnon osaamisvaatimuksiin ja lopuksi saat tuloksena listan vastauksistasi ja graafiset kuvaajat osaamisvaatimusten keskiarvoista. Voit myös lähettää raportin pdf-tiedostona haluamiisi sähköpostiosoitteisiin. (sv)',
      'etusivu.tutkinnon_nimi': 'Tutkinnon nimi (sv)',
      'etusivu.tutkinnon_nimi_placeholder': 'Kirjoita tutkinnon nimi tai nimen osa (sv)',
      'etusivu.otsikko': 'Osaamisen tunnistaminen (sv)',
      'footer.ota_yhteytta': 'osaan.fi käyttöä koskevan palautteen voi lähettää sähköpostilla osoitteeseen (sv)',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Palvelun tarjoaa Opetushallitus (sv)',
      'footer.rekisteriseloste': 'osaan.fi rekisteriselosteen voit lukea osoitteesta:',
      'footer.rekisteriseloste_url': 'http://osaan.fi/Rekisteriseloste%20Osaan%20fi.pdf',
      'osien-valinta.eteenpain': 'Eteenpäin (sv)',
      'osien-valinta.pakolliset_osat': 'Pakolliset osat (sv)',
      'osien-valinta.valinnaiset_osat': 'Valinnaiset osat (sv)',
      'raportti.arvio': 'Arvio (sv)',
      'raportti.arvioinnin_kohde': 'Arvioinnin kohde (sv)',
      'raportti.kommentti': 'Kommentti (sv)',
      'raportti.otsikko': 'Raportti (sv)',
      'raportti.paivays': 'Päiväys (sv)',
      'raportti.palaa_arviointiin': 'Arvioi osaaminen (sv)'
    }
  })

  .config(['$translateProvider', 'tekstit', function($translateProvider, tekstit) {
    $translateProvider.translations('fi', tekstit.fi);
    $translateProvider.translations('sv', tekstit.sv);

    $translateProvider.use(localStorage.getItem('kieli') || 'fi');
  }])

  .factory('kieli', [function() {
    return localStorage.getItem('kieli') || 'fi';
  }])
;
