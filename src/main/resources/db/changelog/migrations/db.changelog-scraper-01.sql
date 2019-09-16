--liquibase formatted sql

--changeset celio:16
create schema if not exists scraper;

--rollback drop schema scraper;

--changeset celio:17

create table if not exists scraper.orgao
(
  org_id bigserial primary key,
  org_codigo character varying(6),
  org_sigla character varying(30),
  org_orgao character varying(150) not null,
  org_nome character varying(150),
  org_esfera smallint not null
);

--rollback drop table scraper.orgao;

--changeset celio:18

create table if not exists scraper.fonte
(
  fon_id character varying(8) primary key,
  fon_nome character varying(100) not null
);

--rollback drop table scraper.fonte;

--changeset celio:19

create table if not exists scraper.credor
(
  cre_id bigserial primary key,
  cre_nome character varying(100) not null unique,
  cre_codigo character varying(20)
);

--rollback drop table scraper.credor;

--changeset celio:20

create table if not exists scraper.classificacao
(
  cla_id bigserial primary key,
  cla_codigo character varying(10),
  cla_nome character varying(100)
);

--rollback drop table scraper.classificacao;

--changeset celio:21

create table if not exists scraper.pagamento
(
  pag_id bigserial primary key,
  pag_date date not null,
  pag_nr_ob character varying(15),
  pag_nr_nl character varying(15),
  pag_nr_ne character varying(15),
  pag_valor numeric(20,2) not null,
  pag_org_id bigint not null,
  pag_cre_id bigint not null,
  pag_fon_id character varying(8) not null,
  pag_cla_id bigint,
  pag_lancamento timestamp without time zone not null default now(),
  pag_removido boolean not null default false,
  foreign key (pag_cla_id) references scraper.classificacao (cla_id),
  foreign key (pag_cre_id) references scraper.credor (cre_id),
  foreign key (pag_fon_id) references scraper.fonte (fon_id),
  foreign key (pag_org_id) references scraper.orgao (org_id)
);

--rollback drop table scraper.pagamento;

--changeset celio:22

create table if not exists scraper.parametro
(
  par_id integer primary key,
  par_descricao character varying(100) not null,
  par_atual character varying(2048),
  par_padrao character varying(2048)
);

--rollback drop table scraper.parametro;

--changeset celio:23

create index if not exists classificacao_cla_nome_idx on scraper.classificacao (cla_nome);

--changeset celio:24

create index if not exists classificacao_cla_codigo_idx on scraper.classificacao (cla_codigo);

--changeset celio:25

create index if not exists fonte_fon_nome_idx on scraper.fonte (fon_nome);

--changeset celio:26

create index if not exists orgao_org_codigo_idx on scraper.orgao (org_codigo);

--changeset celio:27

create index if not exists orgao_org_sigla_idx on scraper.orgao (org_sigla);

--changeset celio:28

create index if not exists orgao_org_orgao_idx on scraper.orgao (org_orgao);

--changeset celio:78

create index if not exists pagamento_pag_date_idx on scraper.pagamento (pag_date);

--changeset celio:79

create index if not exists pagamento_pag_org_id_idx on scraper.pagamento (pag_org_id);

--changeset celio:80

create index if not exists pagamento_pag_cre_id_idx on scraper.pagamento (pag_cre_id);

--changeset celio:81

create index if not exists pagamento_pag_fon_id_idx on scraper.pagamento (pag_fon_id);

--changeset celio:82

create index if not exists pagamento_pag_cla_id_idx on scraper.pagamento (pag_cla_id);
