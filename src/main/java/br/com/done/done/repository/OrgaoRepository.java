package br.com.done.done.repository;

import br.com.done.done.model.Orgao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {

    @Query(value = "select distinct(org.org_codigo), org.org_orgao, sum(rp.pag_valor) " +
            "from scraper.pagamento rp inner join scraper.orgao org on(rp.pag_org_id = org.org_id) " +
            "where rp.pag_date >= ?1 and rp.pag_date <= ?2 and rp.pag_removido = false " +
            "group by org.org_codigo, org.org_orgao order by org.org_orgao", nativeQuery = true)
    List<Object[]> getOrgaoValueByDate(Date dtInicial, Date dtFinal);

    Optional<Orgao> findByCodigo(String codigo);

    @Transactional(readOnly = true)
    @Query(value = "SELECT org.org_sigla sigla, org.org_orgao orgao, sum(pag_valor) valor FROM scraper.pagamento pag " +
            "INNER JOIN scraper.orgao org on(pag.pag_org_id = org.org_id) " +
            "WHERE pag_removido = false AND pag.pag_date >= ?1 AND pag.pag_date <= ?2 " +
            "GROUP BY sigla, orgao ORDER BY valor DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTopFiveOrgaos(java.sql.Date dateInicial, java.sql.Date dateFinal);

    Page<Orgao> findByNomeIgnoreCaseContainingOrderByNomeAsc(String nome, Pageable pageable);
}
