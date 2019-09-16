package br.com.done.done.repository;

import br.com.done.done.model.Pagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("select t from Pagamento t where t.orgao.codigo = ?1 " +
            "and t.data >= ?2 and t.data <= ?3 " +
            "and t.removido = false order by t.credor.nome")
    List<Pagamento> getPagamentos(String orgao, Date inicio, Date fim);

    @Transactional(readOnly = true)
    Page<Pagamento> findAll(Specification spec, Pageable pageable);

    @Transactional(readOnly = true)
    @Query(value = "SELECT date_part('year', pag_date) ano, sum(pag_valor) FROM scraper.pagamento " +
            "WHERE pag_removido = false and pag_date > ?1  GROUP BY ano ORDER BY ano", nativeQuery = true)
    List<Object[]> findFiveYearsPagagmentos(java.sql.Date date);
}
