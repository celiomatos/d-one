package br.com.done.done.repository;

import br.com.done.done.model.EmpenhoOriginal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpenhoOriginalRepository extends JpaRepository<EmpenhoOriginal, Long> {

    Optional<EmpenhoOriginal> findByOriginalIdAndReforcoId(long idOriginal, long idReforco);
}
