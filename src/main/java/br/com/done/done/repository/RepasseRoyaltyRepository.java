package br.com.done.done.repository;

import br.com.done.done.model.RepasseRoyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepasseRoyaltyRepository extends JpaRepository<RepasseRoyalty, Long> {

    List<RepasseRoyalty> findByRepasseIdAndRemovidoFalse(long repasseId);
}
