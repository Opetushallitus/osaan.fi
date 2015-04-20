Osaan.fi 
========

Osaan.fi palvelun uudistettu versio. 



# Paikallinen kehitysympäristö

1. Tietokantapalvelin virtuaalikoneena
```
cd vagrant
vagrant up osaan-db
```

2. Tietokannan pystyttäminen paikallisesti
```
cd osaan-db
lein uberjar
java -jar target/osaan-db-standalone.jar --clear postgresql://osaan_adm:osaan-adm@127.0.0.1:4567/osaan
```
