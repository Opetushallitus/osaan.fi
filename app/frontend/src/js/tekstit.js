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
      'arviointi.arvioi_osaamisesi': 'Arvioi osaamisesi',
      'arviointi.arvioita_puuttuu': 'Et ole antanut arvioita kaikista ammattitaidoista!',
      'arviointi.arvioitu': 'Arvioitu',
      'arviointi.asteikko1': 'En osaa',
      'arviointi.asteikko2': 'Osaan vähän',
      'arviointi.asteikko3': 'Osaan hyvin',
      'arviointi.asteikko4': 'Osaan erinomaisesti',
      'arviointi.ei_kohteita': 'Arviointikohteita ei ole tällä hetkellä saatavissa.',
      'arviointi.eos': 'En osaa sanoa',
      'arviointi.eos_lyh': 'eos',
      'arviointi.miten': 'Miten seuraavat väittämät kuvaavat osaamistasi?',
      'arviointi.raportti': 'Näytä raportti',
      'arviointi.tekstivastaus': 'Tekstivastaus',
      'arviointi.tekstivastaus_placeholder': 'Kirjoita vastauksesi tähän',
      'etusivu.jagkan': 'osaan.fi',
      'etusivu.opintoala': 'Ala',
      'etusivu.otsikko': 'Osaamisen tunnistaminen',
      'etusivu.teksti': 'Voit arvioida osaamistasi valitsemassasi ammatillisessa perustutkinnossa, ammattitutkinnossa tai erikoisammattitutkinnossa.\n\nArviointi tehdään tutkinnoissa edellytettyjen ammattitaitovaatimusten perusteella, jotka on laadittu yhteistyössä työelämän kanssa. Tutkinnon suorittamisessa huomioidaan aiempi osaamisesi, joka voi olla hankittu esimerkiksi työssä, opinnoissa tai muussa toiminnassa. Kun hakeudut suorittamaan tutkintoa, oppilaitos selvittää aiemman osaamisesi. Jos olet kiinnostunut suorittamaan tutkinnon, ole yhteydessä näyttötutkinnon tai koulutuksen järjestäjiin, jotka näet linkkien kautta.\n\nNäyttötutkinto on erityisesti aikuisille suunniteltu tutkinnon suorittamistapa. Ammatillinen peruskoulutus on erityisesti nuorille suunniteltua koulutusta.',
      'etusivu.tutkinnon_nimi': 'Tutkinnon nimi',
      'etusivu.tutkinnon_nimi_placeholder': 'Kirjoita tutkinnon nimi tai nimen osa',
      'footer.ohje': 'Ammattitaitovaatimukset perustuvat voimassa oleviin tutkinnon perusteisiin. Tarkempia tietoja tutkinnon suorittamisesta ja koulutukseen hakeutumisesta löydät oppilaitosten sivuilta ja',
      'footer.ohje_url': 'https://opintopolku.fi/wp/fi/valintojen-tuki/ohjaus-ja-neuvontapalvelut',
      'footer.ohje_url_title': 'Opintopolku.fi-palvelusta.',
      'footer.ota_yhteytta': 'Osaan.fi-palvelun käyttöä koskevan palautteen voi lähettää sähköpostilla osoitteeseen',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Palvelun tarjoaa Opetushallitus',
      'footer.rekisteriseloste': 'Palvelua voi käyttää anonyymisti.',
      'footer.rekisteriseloste_url': 'img/Rekisteriseloste Osaan fi_14_022_2015.pdf',
      'footer.rekisteriseloste_url_title': 'Rekisteriseloste',
      'http-virhe.ei_yhteytta': 'Palvelimeen ei juuri nyt saada yhteyttä. Palvelu yrittää automaattisesti uudelleen hetken kuluttua.',
      'http-virhe.virhe': 'Palvelimella tapahtui virhe.',
      'http-virhe.yrita_uudelleen': 'Yritä ladata sivu uudelleen.',
      'lataa.palaa_etusivulle': 'Palaa etusivulle',
      'lataa.virhe': 'Virheellinen latauslinkki.',
      'osien-valinta.arvio_ladattu': '{{luotuaika}} luotu arviointisi on nyt ladattu. Voit jatkaa arviointiasi ja tallentaa uuden arvion.',
      'osien-valinta.arvio_ladattu_otsikko': 'Arvio on ladattu',
      'osien-valinta.arvioita_on': 'Valituille osille löytyy arvioita',
      'osien-valinta.eteenpain': 'Eteenpäin',
      'osien-valinta.katso-ohje': 'Katso tutkinnon perusteista tai selvitä yhdessä oppilaitoksen tai oppisopimustoimipisteen kanssa mitkä tutkinnon osat voit valita, jos tavoitteenasi on koko tutkinnon suorittaminen.',
      'osien-valinta.pakolliset_osat': 'Pakolliset osat',
      'osien-valinta.tyhjenna_arviot': 'Tyhjennä arviot',
      'osien-valinta.tyhjenna_arviot_otsikko': 'Haluatko varmasti tyhjentää jo annetut arviot?',
      'osien-valinta.tyhjenna_arviot_varoitus': 'Arvioita ei voi palauttaa.',
      'osien-valinta.tyhjenna-teksti': 'Jos haluat arvoida näiden osien osaamisesi uudestaan voit tyhjentää aiemmat arviosi',
      'osien-valinta.valinnaiset_osat': 'Valinnaiset osat',
      'osien-valinta.yhteiset_osat': 'Yhteiset osat',
      'raportti.ammattitaidon_kuvaus': 'Ammattitaidon kuvaus',
      'raportti.arvio': 'Arvio',
      'raportti.eos_vastauksia': 'joista eos-vastauksia',
      'raportti.hierarkia': 'Tutkinnon osa -> kohdealue -> ammattitaito',
      'raportti.keskiarvot': 'Arviointien keskiarvot tutkinnon osittain',
      'raportti.kommentti': 'Kommentti',
      'raportti.lataa_tekstiraportti': 'Lataa tekstiraportti',
      'raportti.lataa_tekstiraportti_ohje': 'Lataa raportin koneellesi ilman grafiikkaa tai muotoiluja, jolloin se on siirrettävissä toiseen ohjelmaan esimerkiksi muokattavaksi.',
      'raportti.nayta_kommentit': 'Näytä kaikki kommentit',
      'raportti.otsikko_ohje': 'Tutkinto suoritetaan Opetushallituksen määräämien tutkinnon perusteiden mukaisesti. Tutkinnon perusteissa määritellään tutkintoon kuuluvat tutkinnon osat, tutkinnon muodostuminen, vaadittava ammattitaito, arvioinnin perusteet sekä ammattitaidon osoittamistavat. Tutkinnon perusteet näet osoitteesta http://eperusteet.opintopolku.fi/. Tutkinnon muodostumisesta lisätietoja antavat kyseistä tutkintoa järjestävät oppilaitokset sekä oppisopimustoimipisteet.',
      'raportti.paivays': 'Päiväys',
      'raportti.pakolliset': 'Pakolliset',
      'raportti.tulosta': 'Tulosta',
      'raportti.tulosta_ohje': 'Tulostaa selaimen Tulosta-toiminnolla raportin paperille tai muuhun muotoon, esimerkiksi PDF-tiedostoksi.',
      'raportti.tutkinnon_keskiarvo': 'Tutkinnon keskiarvo',
      'raportti.tyyppi_tekstiraportti': 'Tekstiraportti',
      'raportti.valinnaiset': 'Valinnaiset',
      'raportti.valintojen_selitykset': 'Numeroiden selitykset:',
      'raportti.valittu': 'valittu',
      'raportti.vastausprosentit': 'Vastausprosentit',
      'raportti.tulostus': 'Henkilökohtainen arvio. Tulostettu Opetushallituksen Osaan.fi-palvelusta.',
      'tallennus.kopioi_ohje': 'Valitse linkki ja kopioi se leikepöydälle painamalla:',
      'tallennus.laheta_sahkopostilla': 'Avaa sähköposti, jos sellainen on käytettävissäsi',
      'tallennus.otsikko': 'Arvio tallennettu',
      'tallennus.sulje': 'Sulje',
      'tallennus.teksti1': 'Vastauksesi on nyt tallennettu. Voit palata katsomaan, muuttamaan tai täydentämään sitä alla olevan linkin kautta.',
      'tallennus.teksti2': 'Tallenna linkki itsellesi. Pääset palaamaan arvioosi kopioimalla linkin selaimen osoiteriville. Jos muutat tai täydennät vastauksiasi, jokainen uusi tallennus luo uuden linkin. Myös aikaisemmat arviosi ovat käytettävissäsi aiemmin saatujen linkkien kautta. Tallenteet ovat anonyymejä ja niitä säilytetään tallennushetkestä kolmen vuoden ajan, jonka jälkeen ne poistuvat järjestelmästä.\n\nJos olet sopinut esimerkiksi oppilaitoksen tai oppisopimustoimiston kanssa, että lähetät vastauksesi heille, voit lähettää tämän linkin.',
      'tutkinto.ei_loytynyt': 'Tutkintoja ei löytynyt',
      'tutkinto.eperusteet_linkki': 'Katso tutkinnon perusteet ePerusteet-järjestelmässä',
      'tutkinto.eperusteet_linkki_info': 'Tutkinnon perusteasiakirjasta näet, mistä pakollisista ja valinnaisista tutkinnon osista tutkinto muodostuu sekä tarkempaa tietoa tutkinnosta ja sen vaatimuksista.',
      'tutkinto.koulutustarjonta_linkki': 'Katso koulutuksen järjestäjät',
      'tutkinto.koulutustarjonta_linkki_info': 'Tahot, jotka järjestävät ammatillista peruskoulutusta tähän tutkintoon. Ammatillinen peruskoulutus on erityisesti nuorille suunniteltu tutkinnon suorittamistapa.',
      'tutkinto.nayttotutkinnonjarjestajat_linkki': 'Katso näyttötutkinnon järjestäjät',
      'tutkinto.nayttotutkinnonjarjestajat_linkki_info': 'Tahot, jotka järjestävät tätä tutkintoa näyttötutkintona. Näyttötutkinto on erityisesti aikuisille suunniteltu tutkinnon suorittamistapa.',
      'tutkinto.otsikko': 'Aloita etsimällä ja valitsemalla haluamasi tutkinto tai ala',
      'tutkinto.peruste_tyyppi_naytto': 'Näyttötutkinto',
      'tutkinto.peruste_tyyppi_ops': 'Peruskoulutus',
      'tutkinto.tyyppi': 'Tutkintotyyppi',
      'tutkinto.tyyppi_ammattitutkinto': 'Ammattitutkinto',
      'tutkinto.tyyppi_erikoisammattitutkinto': 'Erikoisammattitutkinto',
      'tutkinto.tyyppi_kaikki': 'Kaikki',
      'tutkinto.tyyppi_perustutkinto': 'Perustutkinto',
      'tutkinto.vanha_osaanfi': 'Jos et löydä hakemaasi tutkintoa, se löytyy vanhasta Osaan.fi-palvelusta. Kaikkien tutkintojen tiedot siirtyvät vähitellen uuteen palveluun.',
      'tutkinto.vanha_osaanfi_osoite': 'http://vanha.osaan.fi/',
      'tutkinto.voimaantulevat': 'Näytä myös tutkinnot, joiden tutkinnon perusteet ovat tulossa voimaan.',
      'tutkinto_osa.valitse': 'Valitse tutkinnon osat jotka haluat arvioida',
      'varmistus.peruuta': 'Peruuta',
      'vastaukset-muistissa': 'Vastauksesi ovat muistissa siihen asti kun lopetat palvelun käytön. Jos haluat tallentaa tähän tutkintoon tekemäsi arvion, valitse Tallenna arviot.',
      'yleiset.arvioi_osaaminen': 'Arvioi osaaminen',
      'yleiset.hakutulosta': 'hakutulosta',
      'yleiset.kielen_vaihto': 'På svenska',
      'yleiset.raportti': 'Raportti',
      'yleiset.sivun_loppuun': 'Sivun loppuun',
      'yleiset.tallenna': 'Tallenna arviot',
      'yleiset.valitse_tutkinnon_osat': 'Valitse tutkinnon osat',
      'yleiset.valitse_tutkinto': 'Valitse tutkinto'
    },
    sv: {
      'arviointi.arvioi_osaamisesi': 'Bedöm ditt kunnande',
      'arviointi.arvioita_puuttuu': 'Du har inte bedömt alla yrkeskompetenser!',
      'arviointi.arvioitu': 'Bedömd',
      'arviointi.asteikko1': 'Jag kan inte',
      'arviointi.asteikko2': 'Jag kan lite',
      'arviointi.asteikko3': 'Jag kan väl',
      'arviointi.asteikko4': 'Jag kan utmärkt',
      'arviointi.ei_kohteita': 'Inga föremål för bedömningen finns för närvarande.',
      'arviointi.eos': 'Svårt att säga',
      'arviointi.eos_lyh': 'vet ej',
      'arviointi.miten': 'Hur beskriver följande påståenden ditt kunnande?',
      'arviointi.raportti': 'Visa rapporten',
      'arviointi.tekstivastaus': 'Textsvar',
      'arviointi.tekstivastaus_placeholder': 'Skriv ditt svar här',
      'etusivu.jagkan': 'Jagkan.fi',
      'etusivu.opintoala': 'Bransch',
      'etusivu.otsikko': 'Erkännande av kunnande',
      'etusivu.teksti': 'Du kan bedöma ditt eget kunnande inom en vald yrkesinriktad grundexamen, yrkesexamen eller specialyrkesexamen.\n\nBedömningen görs utgående från de krav på yrkesskicklighet som förutsätts för examen och som sammanställts i samarbete med arbetslivet. Då man avlägger en examen beaktas tidigare kunnande som t.ex. förvärvats i arbete, studier eller i annan verksamhet. När du ämnar avlägga en examen, utreder läroanstalten ditt tidigare kunnande. Ta kontakt med anordnaren av den fristående examen eller utbildningen som du ser via länken om du är intresserad av att avlägga en examen.\n\nEn fristående examen är planerad särskilt med tanke på vuxna. Den grundläggande yrkesutbildningen är planerad för unga.',
      'etusivu.tutkinnon_nimi': 'Namn på examen',
      'etusivu.tutkinnon_nimi_placeholder': 'Skriv namnet på examen eller en del av namnet',
      'footer.ohje': 'Kraven på yrkesskicklighet baserar sig på de examensgrunder som är i kraft. Noggrannare information om att avlägga en examen eller att söka till studier hittar du på läroanstalternas sidor via Studieinfo.fi-tjänsten.',
      'footer.ohje_url': 'https://opintopolku.fi/wp/sv/stod-for-studievalet/radgivning-och-handledning/',
      'footer.ohje_url_title': 'Om Studieinfo.fi-tjänsten.',
      'footer.ota_yhteytta': 'Respons om andvändningen av Jagkan.fi kan du skicka per e-post till adressen aitu-tuki@oph.fi',
      'footer.ota_yhteytta_email': 'aitu-tuki@oph.fi',
      'footer.otsikko': 'Servicen erbjuds av Utbildningsstyrelsen',
      'footer.rekisteriseloste': 'Servicen kan användas anonymt. ',
      'footer.rekisteriseloste_url': 'img/Registerbeskrivning_Jagkan_14_22_2015.pdf',
      'footer.rekisteriseloste_url_title': 'Registerbeskrivning',
      'http-virhe.ei_yhteytta': 'Servern kan inte nås just nu. Tjänsten försöker automatiskt nå servern på nytt om en stund.',
      'http-virhe.virhe': 'Ett fel uppstod i servern.',
      'http-virhe.yrita_uudelleen': 'Försök ladda sidan på nytt.',
      'lataa.palaa_etusivulle': 'Återvänd till första sidan',
      'lataa.virhe': 'Felaktig nedladdningslänk.',
      'osien-valinta.arvio_ladattu': '{{luotuaika}} Bedömningen som du gjort har nu laddats ned. Du kan fortsätta bedömningen och lagra en ny bedömning. ',
      'osien-valinta.arvio_ladattu_otsikko': 'Bedömningen är nedladdad',
      'osien-valinta.arvioita_on': 'Det finns färdiga bedömningar för de valda delarna',
      'osien-valinta.eteenpain': 'Vidare',
      'osien-valinta.katso-ohje': 'Se i examensgrunderna eller utred tillsammans med läroanstalten eller läroavtalsbyrån vilka examensdelar du kan välja, om du har för avsikt att avlägga en hel examen.',
      'osien-valinta.pakolliset_osat': 'Obligatoriska delar',
      'osien-valinta.tyhjenna_arviot': 'Radera bedömningarna',
      'osien-valinta.tyhjenna_arviot_otsikko': 'Vill du säkert radera de gjorda bedömningarna?',
      'osien-valinta.tyhjenna_arviot_varoitus': 'Bedömningarna kan inte åter',
      'osien-valinta.tyhjenna-teksti': 'Om du vill bedöma ditt kunnande i dessa delar på nytt kan du radera dina tidigare bedömningar',
      'osien-valinta.valinnaiset_osat': 'Valbara delar',
      'osien-valinta.yhteiset_osat': 'Gemensamma delar',
      'raportti.ammattitaidon_kuvaus': 'Beskrivning av yrkesskickligheten',
      'raportti.arvio': 'Bedömning',
      'raportti.eos_vastauksia': 'var av vet ej -svar',
      'raportti.hierarkia': 'Examensdel -> delområde -> yrkesskicklighet',
      'raportti.keskiarvot': 'Medeltal för bedömningen enligt examensdelar.',
      'raportti.kommentti': 'Kommentar',
      'raportti.lataa_tekstiraportti': 'Ladda ned textrapporten',
      'raportti.lataa_tekstiraportti_ohje': 'Ladda ner rapporten till din egen dator utan grafik och formgivning, så att den kan överföras till ett annat program t.ex. för bearbetning.',
      'raportti.nayta_kommentit': 'Visa alla kommentarer',
      'raportti.otsikko_ohje': 'Examen avläggs enligt de examensgrunder som fastställts av Utbildningsstyrelsen. I grunderna för examen definieras examensdelarna, uppbyggnaden av examen, kraven på yrkesskicklighet, bedömningsgrunderna och sätten att påvisa sina yrkeskunskaper. Examensgrunderna hittar du via adressen egrunder.studieinfo.fi. Mer info om uppbyggnaden av en viss examen får du från de läroanstalter och läroavtalsbyråer som anordnar examen.',
      'raportti.paivays': 'Datum',
      'raportti.pakolliset': 'Obligatoriska',
      'raportti.tulosta': 'Skriv ut',
      'raportti.tulosta_ohje': 'Via Skriv ut-funktionen skrivs rapporten ut på papper eller i annan form, t.ex. som PDF-fil.',
      'raportti.tutkinnon_keskiarvo': 'Medeltal på examen',
      'raportti.tyyppi_tekstiraportti': 'Textrapport',
      'raportti.valinnaiset': 'Valbara',
      'raportti.valintojen_selitykset': 'Siffrornas förklaringar',
      'raportti.valittu': 'vald',
      'raportti.vastausprosentit': 'Svarsprocent',
      'raportti.tulostus': 'Personliga bedömningen. Utskrift från Utbildningsstyrelsens Jagkan.fi-tjänst.',
      'tallennus.kopioi_ohje': 'Välj länken och kopiera den till klippbordet genom:',
      'tallennus.laheta_sahkopostilla': 'Öppna din e-post om du har tillgång till en sådan.',
      'tallennus.otsikko': 'Bedömningen har lagrats',
      'tallennus.sulje': 'Stäng',
      'tallennus.teksti1': 'Bedömningen och dina svar har nu sparats. Du kan återvända till bedömningen via länken nedan och se, ändra eller komplettera den.',
      'tallennus.teksti2': 'Spara länken åt dig själv. Du kan återvända till bedömningen genom att kopiera länken till adressfältet i din webbläsare. Om du ändrar eller kompletterar dina svar, skapar varje ny lagring en ny länk. Också dina tidigare bedömningar finns kvar via de länkar som du fått tidigare. Bedömningarna som lagrats är anonyma och sparas i tre år fr.o.m. lagringsögonblicket. Därefter raderas svaren ur systemet.\n\nOm du t.ex. har kommit överens om att skicka dina svar till en läroanstalt eller läroavtalsbyrå, kan du skicka denna länk.',
      'tutkinto.ei_loytynyt': 'Inga examina hittades',
      'tutkinto.eperusteet_linkki': 'Se examensgrunderna i eGrunder-systemet',
      'tutkinto.eperusteet_linkki_info': 'I examensgrunderna ser du vilka obligatoriska och valbara delar examen består av och noggrannare information om examen och examenskraven',
      'tutkinto.koulutustarjonta_linkki': 'Se utbildningsanordnarna',
      'tutkinto.koulutustarjonta_linkki_info': 'Instanser som anordnar grundläggande yrkesutbildning för denna examen. Grundläggande yrkesutbildning är ett sätt att avlägga en examen som är planerat med tanke på ungdomar.',
      'tutkinto.nayttotutkinnonjarjestajat_linkki': 'Visa anordnarna av denna fristående examen',
      'tutkinto.nayttotutkinnonjarjestajat_linkki_info': 'Instanser som anordnar denna examen som fristående examen. En fristående examen är ett sätt att avlägga en examen som är planerat med tanke på vuxna.',
      'tutkinto.otsikko': 'Börja genom att söka en examen eller bransch',
      'tutkinto.peruste_tyyppi_naytto': 'Fristående examen',
      'tutkinto.peruste_tyyppi_ops': 'Yrkesinriktad grundexamen',
      'tutkinto.tyyppi': 'Examenstyp',
      'tutkinto.tyyppi_ammattitutkinto': 'Yrkesexamen',
      'tutkinto.tyyppi_erikoisammattitutkinto': 'Specialyrkesexamen',
      'tutkinto.tyyppi_kaikki': 'Alla',
      'tutkinto.tyyppi_perustutkinto': 'Grundexamen',
      'tutkinto.vanha_osaanfi': 'Om du hittar inte den examen du söker, du hittar den i den gamla versionen av tjänsten Jagkan.fi. Uppgifter på alla examina överflyttas småningom till den nya tjänsten.',
      'tutkinto.vanha_osaanfi_osoite': 'http://vanha.jagkan.fi/sv/',
      'tutkinto.voimaantulevat': 'Visa också de examina vars examensgrunder kommer att träda i kraft.',
      'tutkinto_osa.valitse': 'Välj de examensdelar som du vill bedöma',
      'varmistus.peruuta': 'Avbryt',
      'vastaukset-muistissa': 'Dina svar finns lagrade så länge du använder servicen. Om du vill spara din bedömning inom denna examen, välj Spara bedömningen.',
      'yleiset.arvioi_osaaminen': 'Bedöm kunnandet',
      'yleiset.hakutulosta': 'sökträffar',
      'yleiset.kielen_vaihto': 'Suomeksi',
      'yleiset.raportti': 'Rapport',
      'yleiset.sivun_loppuun': 'Till slutet av sidan',
      'yleiset.tallenna': 'Lagra bedömningarna',
      'yleiset.valitse_tutkinnon_osat': 'Välj examensdelar',
      'yleiset.valitse_tutkinto': 'Välj examen'
    }
  })

  .config(['$translateProvider', 'tekstit', function ($translateProvider, tekstit) {
    $translateProvider.translations('fi', tekstit.fi);
    $translateProvider.translations('sv', tekstit.sv);

	if (localStorage.getItem('kieli') === null) {
	  if (_.includes(['www.jagkan.fi', 'jagkan.fi'], document.location.hostname)) {
	    localStorage.setItem('kieli', 'sv');
	  } else {
  	    localStorage.setItem('kieli', 'fi');
  	  }
	}
    $translateProvider.use(localStorage.getItem('kieli'));

    $translateProvider.useSanitizeValueStrategy('sanitizeParameters');
  }])

  .factory('kieli', [function () {
    return localStorage.getItem('kieli');
  }])
;
