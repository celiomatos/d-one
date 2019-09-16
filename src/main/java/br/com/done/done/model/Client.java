package br.com.done.done.model;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

@Setter
@Entity
@Table(name = "oauth_client", schema = "oauth")
public class Client implements ClientDetails {

    @Id
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "resource_ids", nullable = false)
    private String resourceIds;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "client_scope", nullable = false)
    private String scope;

    @Column(name = "authorized_grant_types", nullable = false)
    private String authorizedGrantTypes;

    @Column(name = "registered_redirect_uri")
    private String registeredRedirectUri;

    @Column(name = "client_authorities")
    private String authorities;

    @Column(name = "access_token_validity_seconds", nullable = false)
    private Integer accessTokenValiditySeconds;

    @Column(name = "refresh_token_validity_seconds", nullable = false)
    private Integer refreshTokenValiditySeconds;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "auto_approve")
    private String autoApprove;

    @Column(name = "secret_required", nullable = false)
    private boolean secretRequired;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return StringUtils.commaDelimitedListToSet(resourceIds);
    }

    @Override
    public boolean isSecretRequired() {
        return getClientSecret() != null && !getClientSecret().isEmpty();
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return scope != null && !scope.isEmpty();
    }

    @Override
    public Set<String> getScope() {
        return StringUtils.commaDelimitedListToSet(scope);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return StringUtils.commaDelimitedListToSet(authorizedGrantTypes);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.commaDelimitedListToSet(registeredRedirectUri);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<String> set = StringUtils.commaDelimitedListToSet(authorities);
        Set<GrantedAuthority> result = new HashSet<>();
        set.forEach(authority -> result.add((GrantedAuthority) () -> authority));
        return result;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return autoApprove != null && autoApprove.equalsIgnoreCase("true");
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {

        String[] array = StringUtils.commaDelimitedListToStringArray(additionalInformation);
        if (array != null && array.length > 0) {
            Map<String, Object> information = new HashMap<>();
            for (String str : array) {
                String[] keyValue = str.split(":");
                if (keyValue.length > 1) {
                    information.put(keyValue[0], keyValue[1]);
                }
            }
            return information;
        }
        return null;
    }
}
