package br.com.done.done.repository;

import br.com.done.done.model.Credor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredorRepository extends JpaRepository<Credor, Long> {

    Optional<Credor> findByNome(String nome);

    Optional<Credor> findByCodigo(String codigo);

    @Transactional(readOnly = true)
    @Query(value = "SELECT cre.cre_nome credor, sum(pag_valor) valor FROM scraper.pagamento pag " +
            "INNER JOIN scraper.credor cre on(pag.pag_cre_id = cre.cre_id) " +
            "WHERE pag_removido = false AND pag.pag_date >= ?1 AND pag.pag_date <= ?2 " +
            "GROUP BY credor ORDER BY valor DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTopFiveCredores(java.sql.Date dateInicial, java.sql.Date dateFinal);

    Page<Credor> findByNomeStartingWithOrderByNomeAsc(String nome, Pageable pageable);

    Page<Credor> findByNomeContainingOrderByNomeAsc(String nome, Pageable pageable);

    Page<Credor> findByNomeContainingAndNomeContainingOrderByNomeAsc(String firstName, String secondName, Pageable pageable);
}
