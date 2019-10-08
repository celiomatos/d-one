--liquibase formatted sql

--changeset celio:scraper-012

create table if not exists scraper.fundo (
	fun_id bigserial primary key,
	fun_nome character varying(150) not null,
	fun_sigla character varying(30) not null
);

--rollback drop table scraper.fundo;

--changeset celio:scraper-013

create table if not exists scraper.repasse_federal (
	ref_id bigserial primary key,
	ref_dia date not null,
	ref_parcela character varying(150) not null,
	ref_valor numeric(20,2) not null,
	ref_tipo character varying(1) not null,
	ref_fun_id bigint not null references scraper.fundo (fun_id),
	ref_mun_id bigint not null references main.municipio (mun_id)
);

--rollback drop table scraper.repasse_federal;

--changeset celio:scraper-014

create index if not exists fundo_nome_idx on scraper.fundo (fun_nome);

--changeset celio:scraper-015

create index if not exists repasse_federal_ref_dia_idx on scraper.repasse_federal (ref_dia);

--changeset celio:scraper-016

create index if not exists repasse_federal_ref_fun_id_idx on scraper.repasse_federal (ref_fun_id);

--changeset celio:scraper-017

create index if not exists repasse_federal_ref_mun_id_idx on scraper.repasse_federal (ref_mun_id);

--changeset celio:scraper-018

insert into scraper.parametro(par_id, par_descricao, par_atual, par_padrao)
    values (6, 'Repasse Federal', '01/2019', 'MM/yyyy');

--rollback delete from scraper.parametro where par_id = 6;

--changeset celio:scraper-019

update main.municipio set mun_nome = 'SANTA ISABEL DO RIO NEGRO' where mun_nome = 'SANTA IZABEL DO RIO NEGRO'
