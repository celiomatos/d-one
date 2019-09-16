package br.com.done.done.repository;

import br.com.done.done.model.Fonte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FonteRepository extends JpaRepository<Fonte, String> {

    Page<Fonte> findByNomeIgnoreCaseContainingOrderByNomeAsc(String nome, Pageable pageable);
}
