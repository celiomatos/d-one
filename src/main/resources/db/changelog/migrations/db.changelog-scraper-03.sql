--liquibase formatted sql

--changeset celio:124

create table if not exists scraper.empenho
(
  emp_id bigserial primary key,
  emp_nota character varying(11),
  emp_date date not null,
  emp_tipo character varying(255),
  emp_programa character varying(255),
  emp_funcao character varying(255),
  emp_sub_funcao character varying(255),
  emp_licitacao character varying(255),
  emp_referencia character varying(255),
  emp_processo character varying(255),
  emp_descricao character varying(100000),
  emp_lancamento timestamp without time zone not null default now(),
  emp_cre_id bigint not null,
  emp_org_id bigint not null,
  emp_fon_id character varying(8) not null,
  foreign key (emp_cre_id) references scraper.credor (cre_id),
  foreign key (emp_fon_id) references scraper.fonte (fon_id),
  foreign key (emp_org_id) references scraper.orgao (org_id)
);

--rollback drop table scraper.empenho;

--changeset celio:167

create index if not exists empenho_emp_date_idx on scraper.empenho (emp_date);

--changeset celio:168

create index if not exists empenho_emp_org_id_idx on scraper.empenho (emp_org_id);

--changeset celio:169

create index if not exists empenho_emp_cre_id_idx on scraper.empenho (emp_cre_id);

--changeset celio:170

create index if not exists empenho_emp_fon_id_idx on scraper.empenho (emp_fon_id);
