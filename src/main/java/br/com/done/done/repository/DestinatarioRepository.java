package br.com.done.done.repository;

import br.com.done.done.model.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinatarioRepository extends JpaRepository<Destinatario, Long> {

    List<Destinatario> findByGrupo(String grupo);
}
