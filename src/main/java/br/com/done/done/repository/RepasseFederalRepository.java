package br.com.done.done.repository;

import br.com.done.done.model.RepasseFederal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RepasseFederalRepository extends JpaRepository<RepasseFederal, Long> {

    List<RepasseFederal> findByDiaBetweenOrderByMunicipioIdAscFundoIdAsc(Date diaInicial, Date diaFinal);
}
