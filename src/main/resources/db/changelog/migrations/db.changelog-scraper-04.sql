--liquibase formatted sql

--changeset celio:173

create table if not exists scraper.empenho_despesa
(
  des_id bigserial primary key,
  des_ano character varying(4) not null,
  des_valor numeric(20,2) not null,
  des_cla_id bigint references scraper.classificacao (cla_id),
  des_lancamento timestamp without time zone not null default now(),
  des_emp_id bigint not null references scraper.empenho (emp_id)
);

--rollback drop table scraper.empenho_despesa;

--changeset celio:199

create index if not exists empenho_despesa_des_ano_idx on scraper.empenho_despesa (des_ano);

--changeset celio:200

create index if not exists empenho_despesa_des_emp_id_idx on scraper.empenho_despesa (des_emp_id);

--changeset celio:203

create table if not exists scraper.empenho_original
(
  ori_id bigserial primary key,
  ori_evento character varying(15) not null,
  ori_valor numeric(20,2),
  ori_descricao character varying(100000),
  ori_lancamento timestamp without time zone not null default now(),
  ori_emp_ref_id bigint not null,
  ori_emp_ori_id bigint not null,
  foreign key (ori_emp_ref_id) references scraper.empenho (emp_id),
  foreign key (ori_emp_ori_id) references scraper.empenho (emp_id)
);

--rollback drop table scraper.empenho_original;

--changeset celio:204

create index if not exists empenho_original_ori_emp_ref_id_idx on scraper.empenho_original (ori_emp_ref_id);

--changeset celio:205

create index if not exists empenho_original_ori_emp_ori_id_idx on scraper.empenho_original (ori_emp_ori_id);
