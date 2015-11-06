delete from tutkintonimike;
alter table tutkintonimike
    drop column tutkinto;

create table peruste_ja_tutkintonimike (
    peruste int not null references peruste(peruste_id),
    tutkintonimike varchar(6) not null references tutkintonimike(nimiketunnus),
    primary key (peruste, tutkintonimike)
);
