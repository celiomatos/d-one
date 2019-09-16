package br.com.done.done.repository;

import br.com.done.done.model.EmpenhoDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpenhoDespesaRepository extends JpaRepository<EmpenhoDespesa, Long> {

    Optional<EmpenhoDespesa> findByEmpenhoIdAndAno(Long idEmpenho, String ano);
}
