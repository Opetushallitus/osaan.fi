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
      'yleiset.kielen_vaihto': 'På svenska',
      'yleiset.tallenna': 'Tallenna arviot',
      'arviointi.arvioita_puuttuu': 'Et ole antanut arvioita kaikista ammattitaidoista!',
      'arviointi.asteikko1': 'En osaa',
      'arviointi.asteikko2': 'Osaan vähän',
      'arviointi.asteikko3': 'Osaan hyvin',
      'arviointi.asteikko4': 'Osaan erinomaisesti',
      'arviointi.arvioi_osaamisesi': 'Arvioi osaamisesi',
      'arviointi.eos': 'En osaa sanoa',
      'arviointi.tekstivastaus': 'Tekstivastaus',
      'arviointi.tekstivastaus_placeholder': 'Kirjoita vastauksesi tähän',
      'arviointi.valitse_osa': 'Valitse tutkinnon osat',
      'arviointi.raportti': 'Näytä raportti',
      'etusivu.opintoala': 'Ala',
      'etusivu.teksti': 'Tällä työkalulla voit arvioida osaamistasi sinua kiinnostavassa ammatissa (Näyttötutkinnossa). Palvelussa voit vastata valitsemasi tutkinnon osaamisvaatimuksiin ja lopuksi saat tuloksena listan vastauksistasi ja graafiset kuvaajat osaamisvaatimusten keskiarvoista. Voit myös lähettää raportin pdf-tiedostona haluamiisi sähköpostiosoitteisiin.',
      'etusivu.tutkinnon_nimi': 'Tutkinnon nimi',
      'etusivu.tutkinnon_nimi_placeholder': 'Kirjoita tutkinnon nimi tai nimen osa',
      'etusivu.otsikko': 'Osaamisen tunnistaminen',
      'footer.ota_yhteytta': 'osaan.fi käyttöä koskevan palautteen voi lähettää sähköpostilla osoitteeseen',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Palvelun tarjoaa Opetushallitus',
      'footer.rekisteriseloste': 'osaan.fi rekisteriselosteen voit lukea osoitteesta:',
      'footer.rekisteriseloste_url': 'http://osaan.fi/Rekisteriseloste%20Osaan%20fi.pdf',
      'lataa.palaa_etusivulle': 'Palaa etusivulle',
      'lataa.virhe': 'Virheellinen latauslinkki.',
      'osien-valinta.arvio_ladattu': 'Arvio on ladattu',
      'osien-valinta.arvioita_on': 'Valituille osille löytyy arvioita',
      'osien-valinta.eteenpain': 'Eteenpäin',
      'osien-valinta.pakolliset_osat': 'Pakolliset osat',
      'osien-valinta.poista_arviot': 'Poista arviot',
      'osien-valinta.poista_arviot_otsikko': 'Haluatko varmasti poistaa jo annetut arviot?',
      'osien-valinta.poista_arviot_varoitus': 'Arvioita ei voi palauttaa.',
      'osien-valinta.valinnaiset_osat': 'Valinnaiset osat',
      'osien-valinta.poisto-teksti': 'Jos haluat arvoida näiden osien osaamisesi uudestaan voit poistaa aiemmat arviosi',
      'polku.arvioi_osaaminen': 'Arvioi osaaminen',
      'polku.raportti': 'Raportti',
      'polku.valitse_tutkinto': 'Valitse tutkinto',
      'polku.valitse_tutkinnon_osat': 'Valitse tutkinnon osat',
      'raportti.ammattitaidon_kuvaus': 'Ammattitaidon kuvaus',
      'raportti.arvio': 'Arvio',
      'raportti.kommentti': 'Kommentti',
      'raportti.otsikko': 'Raportti',
      'raportti.paivays': 'Päiväys',
      'raportti.palaa_arviointiin': 'Arvioi osaaminen',
      'tallennus.otsikko': 'Arvio tallennettu',
      'tallennus.sulje': 'Sulje',
      'tallennus.teksti': 'Arvio on nyt tallennettu. Alla olevasta linkistä löydät tekemäsi arviot.',
      'tutkinto.otsikko': 'Aloita etsimällä ja valitsemalla haluamasi tutkinto',
      'tutkinto.voimaantulevat': 'Näytä myös voimaantulevat',
      'tutkinto.tyyppi': 'Tutkintotyyppi',
      'tutkinto.tyyppi_kaikki': 'Kaikki',
      'tutkinto.tyyppi_erikoisammattitutkinto': 'Erikoisammattitutkinto',
      'tutkinto.tyyppi_perustutkinto': 'Perustutkinto',
      'tutkinto.tyyppi_ammattitutkinto': 'Ammattitutkinto',
      'tutkinto.peruste_tyyppi_ops': 'ops',
      'tutkinto.peruste_tyyppi_naytto': 'Näyttötutkinto',
      'tutkinto.eperusteet_linkki': 'Katso tutkinnon perusteet ePerusteet-järjestelmässä',
      'tutkinto.nayttotutkintojenjarjestajat_linkki': 'Katso näyttötutkintojen järjestäjät',
      'tutkinto.koulutustarjonta_linkki': 'Katso koulutuksen järjestäjät',
      'tutkinto.ei_loytynyt': 'Tutkintoja ei löytynyt',
      'tutkinto_osa.valitse': 'Valitse tutkinnon osat jotka haluat arvioida',
      'tutkinto_osa.arvioi': 'Arvioi osaaminen',
      'tutkinto_osa.valitse_tutkinto': 'Valitse tutkinto',
      'varmistus.peruuta': 'Peruuta'
    },
    sv: {
      'yleiset.kielen_vaihto': 'Suomeksi',
      'yleiset.tallenna': 'Tallenna arviot (sv)',
      'arviointi.arvioita_puuttuu': 'Et ole antanut arvioita kaikista ammattitaidoista! (sv)',
      'arviointi.asteikko1': 'En osaa (sv)',
      'arviointi.asteikko2': 'Osaan vähän (sv)',
      'arviointi.asteikko3': 'Osaan hyvin (sv)',
      'arviointi.asteikko4': 'Osaan erinomaisesti (sv)',
      'arviointi.arvioi_osaamisesi': 'Arvioi osaamisesi (sv)',
      'arviointi.eos': 'En osaa sanoa (sv)',
      'arviointi.tekstivastaus': 'Tekstivastaus (sv)',
      'arviointi.tekstivastaus_placeholder': 'Kirjoita vastauksesi tähän (sv)',
      'arviointi.valitse_osa': 'Valitse tutkinnon osat (sv)',
      'arviointi.raportti': 'Näytä raportti (sv)',
      'etusivu.opintoala': 'Ala (sv)',
      'etusivu.teksti': 'Tällä työkalulla voit arvioida osaamistasi sinua kiinnostavassa ammatissa (Näyttötutkinnossa). Palvelussa voit vastata valitsemasi tutkinnon osaamisvaatimuksiin ja lopuksi saat tuloksena listan vastauksistasi ja graafiset kuvaajat osaamisvaatimusten keskiarvoista. Voit myös lähettää raportin pdf-tiedostona haluamiisi sähköpostiosoitteisiin. (sv)',
      'etusivu.tutkinnon_nimi': 'Tutkinnon nimi (sv)',
      'etusivu.tutkinnon_nimi_placeholder': 'Kirjoita tutkinnon nimi tai nimen osa (sv)',
      'etusivu.otsikko': 'Osaamisen tunnistaminen (sv)',
      'footer.ota_yhteytta': 'osaan.fi käyttöä koskevan palautteen voi lähettää sähköpostilla osoitteeseen (sv)',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Palvelun tarjoaa Opetushallitus (sv)',
      'footer.rekisteriseloste': 'osaan.fi rekisteriselosteen voit lukea osoitteesta: (sv)',
      'footer.rekisteriseloste_url': 'http://osaan.fi/Rekisteriseloste%20Osaan%20sv.pdf',
      'lataa.palaa_etusivulle': 'Palaa etusivulle (sv)',
      'lataa.virhe': 'Virheellinen latauslinkki. (sv)',
      'osien-valinta.arvio_ladattu': 'Arvio on ladattu (sv)',
      'osien-valinta.arvioita_on': 'Valituille osille löytyy arvioita (sv)',
      'osien-valinta.eteenpain': 'Eteenpäin (sv)',
      'osien-valinta.pakolliset_osat': 'Pakolliset osat (sv)',
      'osien-valinta.poista_arviot': 'Poista arviot (sv)',
      'osien-valinta.poista_arviot_otsikko': 'Haluatko varmasti poistaa jo annetut arviot? (sv)',
      'osien-valinta.poista_arviot_varoitus': 'Arvioita ei voi palauttaa. (sv)',
      'osien-valinta.valinnaiset_osat': 'Valinnaiset osat (sv)',
      'osien-valinta.poisto-teksti': 'Jos haluat arvoida näiden osien osaamisesi uudestaan voit poistaa aiemmat arviosi (sv)',
      'polku.arvioi_osaaminen': 'Arvioi osaaminen (sv)',
      'polku.raportti': 'Raportti (sv)',
      'polku.valitse_tutkinto': 'Valitse tutkinto (sv)',
      'polku.valitse_tutkinnon_osat': 'Valitse tutkinnon osat (sv)',
      'raportti.ammattitaidon_kuvaus': 'Ammattitaidon kuvaus (sv)',
      'raportti.arvio': 'Arvio (sv)',
      'raportti.kommentti': 'Kommentti (sv)',
      'raportti.otsikko': 'Raportti (sv)',
      'raportti.paivays': 'Päiväys (sv)',
      'raportti.palaa_arviointiin': 'Arvioi osaaminen (sv)',
      'tallennus.otsikko': 'Arvio tallennettu',
      'tallennus.sulje': 'Sulje',
      'tallennus.teksti': 'Arvio on tallennettu. Voit nyt kopioida alla olevan linkin.',
      'tutkinto.otsikko': 'Aloita etsimällä ja valitsemalla haluamasi tutkinto (sv)',
      'tutkinto.voimaantulevat': 'Näytä myös voimaantulevat (sv)',
      'tutkinto.tyyppi': 'Tutkintotyyppi (sv)',
      'tutkinto.tyyppi_kaikki': 'Kaikki (sv)',
      'tutkinto.tyyppi_erikoisammattitutkinto': 'Erikoisammattitutkinto (sv)',
      'tutkinto.tyyppi_perustutkinto': 'Perustutkinto (sv)',
      'tutkinto.tyyppi_ammattitutkinto': 'Ammattitutkinto (sv)',
      'tutkinto.peruste_tyyppi_ops': 'ops (sv)',
      'tutkinto.peruste_tyyppi_naytto': 'Näyttötutkinto (sv)',
      'tutkinto.eperusteet_linkki': 'Katso tutkinnon perusteet ePerusteet-järjestelmässä (sv)',
      'tutkinto.nayttotutkintojenjarjestajat_linkki': 'Katso näyttötutkintojen järjestäjät (sv)',
      'tutkinto.koulutustarjonta_linkki': 'Katso koulutuksen järjestäjät (sv)',
      'tutkinto.ei_loytynyt': 'Tutkintoja ei löytynyt (sv)',
      'tutkinto_osa.valitse': 'Valitse tutkinnon osat jotka haluat arvioida (sv)',
      'tutkinto_osa.arvioi': 'Arvioi osaaminen (sv)',
      'tutkinto_osa.valitse_tutkinto': 'Valitse tutkinto (sv)',
      'varmistus.peruuta': 'Peruuta (sv)'
    }
  })

  .config(['$translateProvider', 'tekstit', function ($translateProvider, tekstit) {
    $translateProvider.translations('fi', tekstit.fi);
    $translateProvider.translations('sv', tekstit.sv);

    $translateProvider.use(localStorage.getItem('kieli') || 'fi');
  }])

  .factory('kieli', [function () {
    return localStorage.getItem('kieli') || 'fi';
  }])
;
