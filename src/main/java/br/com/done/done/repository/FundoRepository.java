package br.com.done.done.repository;

import br.com.done.done.model.Fundo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundoRepository extends JpaRepository<Fundo, Long> {

    Optional<Fundo> findByNomeIgnoreCase(String nome);
}
