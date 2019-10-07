--liquibase formatted sql

--changeset celio:scraper-001

create table if not exists scraper.repasse (
	rep_id bigserial primary key,
	rep_referencia date,
	rep_mun_id bigint not null references main.municipio (mun_id)
);

--rollback drop table scraper.empenho_despesa;

--changeset celio:scraper-002

create table if not exists scraper.repasse_icms (
	icm_id bigserial primary key,
	icm_dtarrecadacao_inicial date not null,
	icm_dtarrecadacao_final date not null,
	icm_dtcredito date not null,
	icm_valor_repasse_icms numeric(20,2) not null,
	icm_dtlancamento timestamp without time zone not null default now(),
	icm_removido boolean not null default false,
	icm_rep_id bigint not null references scraper.repasse (rep_id)
);

--rollback drop table scraper.repasse;

--changeset celio:scraper-003

create table if not exists scraper.repasse_royalty (
	roy_id bigserial primary key,
	roy_royalty varchar(100) not null,
	roy_dtcredito date not null,
	roy_valor_bruto numeric(20,2) not null,
	roy_pasep numeric(20,2) not null,
	roy_dtlancamento timestamp without time zone not null default now(),
	roy_removido boolean not null default false,
	roy_rep_id bigint not null references scraper.repasse (rep_id)
);

--rollback drop table scraper.repasse_royalty;

--changeset celio:scraper-004

create table if not exists scraper.repasse_ipva (
	ipv_id bigserial primary key,
	ipv_dtarrecadacao_inicial date not null,
	ipv_dtarrecadacao_final date not null,
	ipv_valor_repasse_ipva numeric(20,2) not null,
	ipv_dtlancamento timestamp without time zone not null default now(),
	ipv_removido boolean not null default false,
	ipv_rep_id bigint not null references scraper.repasse (rep_id)
);

--rollback drop table scraper.repasse_ipva;

--changeset celio:scraper-005

create table if not exists scraper.repasse_ipi (
	ipi_id bigserial primary key,
	ipi_ipi varchar(100) not null,
	ipi_dtcredito date not null,
	ipi_valor_bruto numeric(20,2) not null,
	ipi_fundo numeric(20,2) not null,
	ipi_pasep numeric(20,2) not null,
	ipi_dtlancamento timestamp without time zone not null default now(),
	ipi_removido boolean not null default false,
	ipi_rep_id bigint not null references scraper.repasse (rep_id)
);

--rollback drop table scraper.repasse_ipi;

--changeset celio:scraper-006

create index if not exists repasse_mun_id_idx on scraper.repasse (rep_mun_id);

--changeset celio:scraper-007

create index if not exists repasse_icms_rep_id_idx on scraper.repasse_icms (icm_rep_id);

--changeset celio:scraper-008

create index if not exists repasse_royalty_rep_id_idx on scraper.repasse_royalty (roy_rep_id);

--changeset celio:scraper-009

create index if not exists repasse_ipva_rep_id_idx on scraper.repasse_ipva (ipv_rep_id);

--changeset celio:scraper-010

create index if not exists repasse_ipi_rep_id_idx on scraper.repasse_ipi (ipi_rep_id);


--changeset celio:scraper-011

insert into scraper.parametro(par_id, par_descricao, par_atual, par_padrao)
    values (1, 'Repasse Estadual', '01/2019', 'MM/yyyy');

--rollback delete from scraper.parametro where par_id = 1;
