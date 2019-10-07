package br.com.done.done.repository;

import br.com.done.done.model.RepasseIcms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepasseIcmsRepository extends JpaRepository<RepasseIcms, Long> {

    List<RepasseIcms> findByRepasseIdAndRemovidoFalse(long repasseId);
}
