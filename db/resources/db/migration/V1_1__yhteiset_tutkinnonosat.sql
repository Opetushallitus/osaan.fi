alter table tutkinnonosa_ja_peruste add column tyyppi varchar(20);
update tutkinnonosa_ja_peruste set tyyppi = 'pakollinen' where pakollinen;
update tutkinnonosa_ja_peruste set tyyppi = 'valinnainen' where not pakollinen;
alter table tutkinnonosa_ja_peruste drop column pakollinen;
