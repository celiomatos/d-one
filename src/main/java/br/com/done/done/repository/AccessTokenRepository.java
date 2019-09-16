package br.com.done.done.repository;

import br.com.done.done.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

    List<AccessToken> findByClientId(String clientId);

    List<AccessToken> findByClientIdAndUserName(String clientId, String userName);

    Optional<AccessToken> findByTokenId(String tokenId);

    Optional<AccessToken> findByRefreshToken(String refreshToken);

    Optional<AccessToken> findByAuthenticationId(String authenticationId);

}
