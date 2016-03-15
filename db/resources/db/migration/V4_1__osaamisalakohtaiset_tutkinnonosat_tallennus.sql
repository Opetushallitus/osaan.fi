alter table arvio_tutkinnonosa
add column osaamisala varchar(10) references osaamisala(osaamisalatunnus);