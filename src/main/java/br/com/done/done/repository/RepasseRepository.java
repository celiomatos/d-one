package br.com.done.done.repository;

import br.com.done.done.model.Repasse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RepasseRepository extends JpaRepository<Repasse, Long> {

    Optional<Repasse> findByReferenciaAndMunicipioId(Date referencia, long idmunicipio);
}
