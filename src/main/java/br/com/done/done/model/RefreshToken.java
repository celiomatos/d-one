package br.com.done.done.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
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
@Table(name = "oauth_refresh_token", schema = "oauth")
public class RefreshToken implements Serializable {

    @Id
    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "refresh_token", nullable = false)
    private byte[] refreshToken;

    @Column(name = "authentication", nullable = false)
    private byte[] authentication;

    public OAuth2Authentication getAuthenticationDeserialize() {
        return SerializationUtils.deserialize(this.authentication);
    }

    public void setAuthenticationSerialize(OAuth2Authentication authentication) {
        this.authentication = SerializationUtils.serialize(authentication);
    }

    public OAuth2RefreshToken getTokenDeserialize() {
        return SerializationUtils.deserialize(refreshToken);
    }

    public void setTokenSerialize(OAuth2RefreshToken refreshToken) {
        this.refreshToken = SerializationUtils.serialize(refreshToken);
    }
}
