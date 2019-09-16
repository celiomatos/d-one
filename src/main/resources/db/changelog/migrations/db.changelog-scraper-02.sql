--liquibase formatted sql

--changeset celio:162

insert into scraper.parametro(par_id, par_descricao, par_atual, par_padrao)
    values (2, 'Pagamento Meses Anteriores', '01/2019', 'MM/yyyy');

--rollback delete from scraper.parametro where par_id = 2;

--changeset celio:122

insert into scraper.parametro(par_id, par_descricao, par_atual, par_padrao)
    values (3, 'Atualização de Notas de Empenho Anos Anteriores', '2018-2-', 'ano-idx-orgao01;orgao02;orgaoN');

--rollback delete from scraper.parametro where par_id = 3;

--changeset celio:123

insert into scraper.parametro(par_id, par_descricao, par_atual, par_padrao)
    values (4, 'Atualização de Notas de Empenho Ano Atual', '2019-2-', 'anoatual-idx-orgao01;orgao02;orgaoN');

--rollback delete from scraper.parametro where par_id = 4;