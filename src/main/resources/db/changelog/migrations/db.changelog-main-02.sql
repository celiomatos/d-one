--liquibase formatted sql

--changeset celio:main-001
create schema if not exists main;
--rollback drop schema main;

--changeset celio:main-009
create table if not exists main.destinatario
(
  des_id bigserial primary key,
  des_grupo character varying(15) not null,
  des_mail character varying(45) not null,
  unique (des_grupo, des_mail)
);
--rollback drop table  main.destinatario;

--changeset celio:main-010
insert into main.destinatario(des_grupo, des_mail) values ('d-alert', 'celiomatos@live.com');
--rollback delete from main.destinatario where des_grupo = 'd-alert';

--changeset celio:main-011
insert into main.destinatario(des_grupo, des_mail) values ('d-pagamento', 'celiomatos@live.com');
--rollback delete from main.destinatario where des_grupo = 'd-pagamento';

--changeset celio:main-012
create index if not exists destinatario_des_grupo_idx on main.destinatario (des_grupo);
