package br.com.done.done.repository;

import br.com.done.done.model.RepasseIpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepasseIpiRepository extends JpaRepository<RepasseIpi, Long> {

    List<RepasseIpi> findByRepasseIdAndRemovidoFalse(long repasseId);
}
