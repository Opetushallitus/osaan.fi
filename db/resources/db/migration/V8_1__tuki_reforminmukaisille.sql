ALTER TABLE peruste ALTER COLUMN tyyppi TYPE varchar(30);
ALTER TABLE peruste ALTER COLUMN tutkinto TYPE varchar(30);
ALTER TABLE perustetyyppi ALTER COLUMN tunnus TYPE varchar(30);
INSERT INTO perustetyyppi(tunnus, kuvaus) VALUES
  ('reformi', '2018 jälkeen voimaan tulleet perusteet');
