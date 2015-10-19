update ohje set
    teksti_fi = 'Valitse tutkinto, jonka perusteisiin vertaat osaamistasi. Tarkista tutkinnon perusteet ePerusteet-järjestelmästä. ePerusteista selviää myös, mitkä tutkinnon osat sinun tulee valita mikäli olet aikeissa suorittaa koko tutkinnon. Jos et löydä tutkintoa tästä palvelusta, löydät sen vanhasta Osaan.fi:stä, jonne pääset näytöllä näkyvästä linkistä.',
    teksti_sv = 'Välj den examen mot vars examensgrunder du vill jämföra ditt kunnande. Kontrollera grunderna för examen i eGrunderna. Där framgår det också vilka examensdelar du ska välja om du har för avsikt att avlägga hela examen. Om du inte hittar examen i denna tjänst, hittar du den i den gamla Jagkan.fi-tjänsten som du kommer åt genom att klicka på länken som syns på skärmen.'
    where ohjetunniste = 'etusivu';

update ohje set
    teksti_fi = 'Tutkinto muodostuu pakollisista ja valinnaisia osista. ePerusteet-palvelusta näet mitä tutkinnon osia tulee valita, jos haluat suorittaa koko tutkinnon. Myös tutkinnon osan tai osien suorittaminen on mahdollista.',
    teksti_sv = 'En examen består av obligatoriska och valbara delar. I eGrunder-tjänsten ser du vilka examensdelar du ska välja om du har för avsikt att avlägga hela examen. Det är också möjligt att avlägga en eller flera examensdelar.'
    where ohjetunniste = 'osien-valinta';

update ohje set
    teksti_fi = 'Osaaminen arvioidaan asteikolla 1-4. Jos et tunne asiaa, käytä En osaa sanoa -vaihtoehtoa. Voit tarvittaessa täydentää vastaustasi tekstivastauksella. Kaikkiin väittämiin ei ole pakko vastata. Vastausprosentin perusteella näet kuinka moneen väittämään olet vastannut. Sivunumeroista voit siirtyä tutkinnon osasta toiseen. Voit tallentaa vastauksesi missä tahansa vaiheessa ja palata niihin ohjelman antaman linkin kautta. Ota linkki talteen.',
    teksti_sv = 'Kompetensen utvärderas på skalan 1–4. Om du är osäker, kan du välja alternativet Kan inte säga. Vid behov kan du komplettera ditt svar med text. Det är inte obligatoriskt att svara på alla påståenden. Via svarsprocenten kan du se hur många påståenden du besvarat. Via sidnumreringen kan du röra dig från en examensdel till en annan. Du kan när som helst spara dina svar och återkomma till dem via länken. Anteckna länken.'
    where ohjetunniste = 'arviointi';

update ohje set
    teksti_fi = 'Raportti tulostuu näytölle automaattisesti. Jos haluat tallentaa tai tulostaa, käytä selaimen Tallenna- ja Tulosta-toimintoja. Tekstivastaukset tulostuvat vain, jos olet valinnut Näytä kaikki kommentit.

Raportissa näkyy tutkinnon osittain arviointien keskiarvot (1-4). Vastausprosenteista näet, kuinka monta tutkinnon osaa olet valinnut ja kuinka moneen väittämään olet vastannut.

Lataa tekstiraportti -valinnalla saat raportin ilman grafiikkaa ja muotoilua, jolloin se on siirrettävissä toiseen ohjelmaan muokattavaksi.',
    teksti_sv = 'Rapporten skrivs ut automatiskt på skärmen. Om du vill lagra eller skriva ut rapporten på papper, använd Lagra och Skriv ut-funktionerna på din webbläsare. Textsvaren visas endast om du valt Visa alla kommentarer.

I rapporten syns medeltalen för utvärderingen (1–4) enligt examensdelar. Svarsprocenten berättar hur många examensdelar du valt och hur många påståenden du besvarat.

Genom att välja Ladda ner textrapporten får du rapporten utan grafik och formgivning, vilket gör det möjligt att överföra den till andra program, om du vill bearbeta den.'
    where ohjetunniste = 'raportti';
