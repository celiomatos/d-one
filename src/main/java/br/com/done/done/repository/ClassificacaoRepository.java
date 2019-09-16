package br.com.done.done.repository;

import br.com.done.done.model.Classificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classificacao, Long> {

    Optional<Classificacao> findByCodigo(String codigo);

    Page<Classificacao> findByNomeIgnoreCaseContainingOrderByNomeAsc(String nome, Pageable pageable);
}
