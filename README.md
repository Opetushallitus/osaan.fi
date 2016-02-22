Osaan.fi 
========

Osaan.fi palvelun uudistettu versio.

Automaattinen Travis CI ajo: [![Build Status](https://travis-ci.org/Opetushallitus/osaan.fi.svg?branch=master)](https://travis-ci.org/Opetushallitus/osaan.fi)

# Dokumentaatio

Järjestelmän yleiskuvaus, kuvia arkkitehtuurista yms. löytyy CSC:n confluencesta: [osaan.fi dokumentaatio](https://confluence.csc.fi/display/OPHPALV/osaan.fi).

* Automaattisesti generoituva tietokantadokumentaatio: [osaan.fi tietokanta](http://opetushallitus-docs.s3-website-eu-west-1.amazonaws.com/osaan/)
* Palvelimen [Eastwood](https://github.com/jonase/eastwood) analyysi: [osaan.fi uusin Eastwood lint](http://opetushallitus-docs.s3-website-eu-west-1.amazonaws.com/osaan-lint/osaan-warnings.txt)
* Palvelimen [Codox](https://github.com/weavejester/codox) dokumentaatio: [osaan.fi palvelin Clojure doc](http://opetushallitus-docs.s3-website-eu-west-1.amazonaws.com/osaan-doc/)

# Lähdekoodin haku

## Osaan.fi

Hae tämän projektin lähdekoodi esimerkiksi hakemistoon `osaan.fi`.

## Clojure-utils

Hae tämän projektin rinnalle projekti [clojure-utils](https://github.com/Opetushallitus/clojure-utils). Muiden Opetushallituksen Clojure-projektien kanssa yhteistä koodia käytetään git submodulena.


# Paikallinen ympäristö sovelluksen ajoon

Seuraavilla askelilla sovellus voidaan käynnistää paikallisella Unix-koneella.

1. Kehitystyötä varten tarvittavat ohjelmat
  - Java SE JDK
  - [NodeJS](https://nodejs.org/)
  - [Vagrant](https://www.vagrantup.com/)
  - [VirtualBox](https://www.virtualbox.org/)
  - [Ansible](http://www.ansible.com/)
      - [asennusohjeet](http://docs.ansible.com/ansible/intro_installation.html)
      - Ansible on saatavilla vain Unix-koneille
2. Tietokantapalvelimen pystyttäminen

  ```
  dev-scripts/init-db.sh
  ```
3. Sovelluspalvelimen pystyttäminen

  ```
  dev-scripts/init-app.sh
  ```
4. Sovelluksen asentaminen

  ```
  dev-scripts/build.sh
  dev-scripts/deploy.sh
  ```
5. Sovellus löytyy osoitteesta http://192.168.50.72/osaan/

