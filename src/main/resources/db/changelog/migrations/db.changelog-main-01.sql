--liquibase formatted sql

--changeset celio:main-001

create table if not exists main.uf (
	uf_id bigserial primary key,
	uf_sigla varchar(2) not null unique,
	uf_nome varchar(45) not null unique
);

--rollback drop table main.uf;

--changeset celio:main-002

create table if not exists main.calha (
	cal_id bigserial primary key,
	cal_nome varchar(45) not null unique
);

--rollback drop table main.calha;

--changeset celio:main-003

create table if not exists main.municipio (
	mun_id bigserial primary key,
	mun_nome varchar(100) not null unique,
	mun_cal_id bigint references main.calha (cal_id),
	mun_uf_id bigint not null references main.uf (uf_id)
);

--rollback drop table main.municipio;

--changeset celio:main-004

create index if not exists uf_sigla_idx on main.uf (uf_sigla);

--changeset celio:main-005

create index if not exists municipio_mun_nome_idx on main.municipio (mun_nome);

--changeset celio:main-006

insert into main.uf(uf_sigla, uf_nome) values ('AM', 'Amazonas');

--rollback delete from main.uf where uf_id = 1;

--changeset celio:main-007

insert into main.municipio(mun_nome, mun_uf_id) values ('AMATURA',1);

--changeset celio:main-008

insert into main.municipio(mun_nome, mun_uf_id) values ('ANAMA',1);

--changeset celio:main-009

insert into main.municipio(mun_nome, mun_uf_id) values ('ANORI',1);

--changeset celio:main-013

insert into main.municipio(mun_nome, mun_uf_id) values ('APUI',1);

--changeset celio:main-014

insert into main.municipio(mun_nome, mun_uf_id) values ('ATALAIA DO NORTE',1);

--changeset celio:main-015

insert into main.municipio(mun_nome, mun_uf_id) values ('AUTAZES',1);

--changeset celio:main-016

insert into main.municipio(mun_nome, mun_uf_id) values ('BARCELOS',1);

--changeset celio:main-017

insert into main.municipio(mun_nome, mun_uf_id) values ('BARREIRINHA',1);

--changeset celio:main-018

insert into main.municipio(mun_nome, mun_uf_id) values ('BENJAMIN CONSTANT',1);

--changeset celio:main-019

insert into main.municipio(mun_nome, mun_uf_id) values ('BERURI',1);

--changeset celio:main-020

insert into main.municipio(mun_nome, mun_uf_id) values ('BOA VISTA DO RAMOS',1);

--changeset celio:main-021

insert into main.municipio(mun_nome, mun_uf_id) values ('BOCA DO ACRE',1);

--changeset celio:main-022

insert into main.municipio(mun_nome, mun_uf_id) values ('BORBA',1);

--changeset celio:main-023

insert into main.municipio(mun_nome, mun_uf_id) values ('CAAPIRANGA',1);

--changeset celio:main-024

insert into main.municipio(mun_nome, mun_uf_id) values ('CANUTAMA',1);

--changeset celio:main-025

insert into main.municipio(mun_nome, mun_uf_id) values ('CARAUARI',1);

--changeset celio:main-026

insert into main.municipio(mun_nome, mun_uf_id) values ('CAREIRO',1);

--changeset celio:main-027

insert into main.municipio(mun_nome, mun_uf_id) values ('CAREIRO DA VARZEA',1);

--changeset celio:main-028

insert into main.municipio(mun_nome, mun_uf_id) values ('COARI',1);

--changeset celio:main-029

insert into main.municipio(mun_nome, mun_uf_id) values ('CODAJAS',1);

--changeset celio:main-030

insert into main.municipio(mun_nome, mun_uf_id) values ('EIRUNEPE',1);

--changeset celio:main-031

insert into main.municipio(mun_nome, mun_uf_id) values ('ENVIRA',1);

--changeset celio:main-032

insert into main.municipio(mun_nome, mun_uf_id) values ('FONTE BOA',1);

--changeset celio:main-033

insert into main.municipio(mun_nome, mun_uf_id) values ('GUAJARA',1);

--changeset celio:main-034

insert into main.municipio(mun_nome, mun_uf_id) values ('HUMAITA',1);

--changeset celio:main-035

insert into main.municipio(mun_nome, mun_uf_id) values ('IPIXUNA',1);

--changeset celio:main-036

insert into main.municipio(mun_nome, mun_uf_id) values ('IRANDUBA',1);

--changeset celio:main-037

insert into main.municipio(mun_nome, mun_uf_id) values ('ITACOATIARA',1);

--changeset celio:main-038

insert into main.municipio(mun_nome, mun_uf_id) values ('ITAMARATI',1);

--changeset celio:main-039

insert into main.municipio(mun_nome, mun_uf_id) values ('ITAPIRANGA',1);

--changeset celio:main-040

insert into main.municipio(mun_nome, mun_uf_id) values ('JAPURA',1);

--changeset celio:main-041

insert into main.municipio(mun_nome, mun_uf_id) values ('JURUA',1);

--changeset celio:main-042

insert into main.municipio(mun_nome, mun_uf_id) values ('JUTAI',1);

--changeset celio:main-043

insert into main.municipio(mun_nome, mun_uf_id) values ('LABREA',1);

--changeset celio:main-044

insert into main.municipio(mun_nome, mun_uf_id) values ('MANACAPURU',1);

--changeset celio:main-045

insert into main.municipio(mun_nome, mun_uf_id) values ('MANAQUIRI',1);

--changeset celio:main-046

insert into main.municipio(mun_nome, mun_uf_id) values ('MANAUS',1);

--changeset celio:main-047

insert into main.municipio(mun_nome, mun_uf_id) values ('MANICORE',1);

--changeset celio:main-048

insert into main.municipio(mun_nome, mun_uf_id) values ('MARAA',1);

--changeset celio:main-049

insert into main.municipio(mun_nome, mun_uf_id) values ('MAUES',1);

--changeset celio:main-050

insert into main.municipio(mun_nome, mun_uf_id) values ('NHAMUNDA',1);

--changeset celio:main-051

insert into main.municipio(mun_nome, mun_uf_id) values ('NOVA OLINDA DO NORTE',1);

--changeset celio:main-052

insert into main.municipio(mun_nome, mun_uf_id) values ('NOVO AIRAO',1);

--changeset celio:main-053

insert into main.municipio(mun_nome, mun_uf_id) values ('NOVO ARIPUANA',1);

--changeset celio:main-054

insert into main.municipio(mun_nome, mun_uf_id) values ('PARINTINS',1);

--changeset celio:main-055

insert into main.municipio(mun_nome, mun_uf_id) values ('PAUINI',1);

--changeset celio:main-056

insert into main.municipio(mun_nome, mun_uf_id) values ('PRESIDENTE FIGUEIREDO',1);

--changeset celio:main-057

insert into main.municipio(mun_nome, mun_uf_id) values ('RIO PRETO DA EVA',1);

--changeset celio:main-058

insert into main.municipio(mun_nome, mun_uf_id) values ('SANTA IZABEL DO RIO NEGRO',1);

--changeset celio:main-059

insert into main.municipio(mun_nome, mun_uf_id) values ('SANTO ANTONIO DO ICA',1);

--changeset celio:main-060

insert into main.municipio(mun_nome, mun_uf_id) values ('SAO GABRIEL DA CACHOEIRA',1);

--changeset celio:main-061

insert into main.municipio(mun_nome, mun_uf_id) values ('SAO PAULO DE OLIVENCA',1);

--changeset celio:main-062

insert into main.municipio(mun_nome, mun_uf_id) values ('SAO SEBASTIAO DO UATUMA',1);

--changeset celio:main-063

insert into main.municipio(mun_nome, mun_uf_id) values ('SILVES',1);

--changeset celio:main-064

insert into main.municipio(mun_nome, mun_uf_id) values ('TABATINGA',1);

--changeset celio:main-065

insert into main.municipio(mun_nome, mun_uf_id) values ('TAPAUA',1);

--changeset celio:main-066

insert into main.municipio(mun_nome, mun_uf_id) values ('TEFE',1);

--changeset celio:main-067

insert into main.municipio(mun_nome, mun_uf_id) values ('TONANTINS',1);

--changeset celio:main-068

insert into main.municipio(mun_nome, mun_uf_id) values ('UARINI',1);

--changeset celio:main-069

insert into main.municipio(mun_nome, mun_uf_id) values ('URUCARA',1);

--changeset celio:main-070

insert into main.municipio(mun_nome, mun_uf_id) values ('URUCURITUBA',1);
