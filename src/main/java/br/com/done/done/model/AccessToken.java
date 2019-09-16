package br.com.done.done.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "oauth_access_token", schema = "oauth")
public class AccessToken implements Serializable {

    @Id
    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "access_token", nullable = false)
    private byte[] accessToken;

    @Column(name = "authentication_id", nullable = false)
    private String authenticationId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "authentication", nullable = false)
    private byte[] authentication;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public OAuth2Authentication getAuthenticationDeserialize() {
        return SerializationUtils.deserialize(authentication);
    }

    public void setAuthenticationSerialize(OAuth2Authentication authentication) {
        this.authentication = SerializationUtils.serialize(authentication);
    }

    public OAuth2AccessToken getTokenDeserialize() {
        return SerializationUtils.deserialize(accessToken);
    }

    public void setTokenSerialize(OAuth2AccessToken accessToken) {
        this.accessToken = SerializationUtils.serialize(accessToken);
    }
}
