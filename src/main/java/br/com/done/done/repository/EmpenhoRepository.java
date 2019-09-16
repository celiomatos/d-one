package br.com.done.done.repository;

import br.com.done.done.model.Empenho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpenhoRepository extends JpaRepository<Empenho, Long> {

    Optional<Empenho> findByNotaAndOrgaoId(String nota, long idOrgao);
}
