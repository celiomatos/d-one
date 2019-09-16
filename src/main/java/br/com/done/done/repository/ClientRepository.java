package br.com.done.done.repository;

import br.com.done.done.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Client findByClientId(String clientId);
}
