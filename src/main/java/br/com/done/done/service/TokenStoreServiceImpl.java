package br.com.done.done.service;

import br.com.done.done.model.AccessToken;
import br.com.done.done.model.RefreshToken;
import br.com.done.done.repository.AccessTokenRepository;
import br.com.done.done.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TokenStoreServiceImpl implements TokenStore {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken oAuth2AccessToken) {
        return readAuthentication(oAuth2AccessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByTokenId(extractTokenKey(token));
        return accessToken.map(AccessToken::getAuthenticationDeserialize).orElse(null);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

        if (readAccessToken(oAuth2AccessToken.getValue()) != null) {
            removeAccessToken(oAuth2AccessToken);
        }

        AccessToken token = new AccessToken();
        token.setTokenId(extractTokenKey(oAuth2AccessToken.getValue()));
        token.setTokenSerialize(oAuth2AccessToken);
        token.setAuthenticationId(authenticationKeyGenerator.extractKey(oAuth2Authentication));
        token.setUserName(oAuth2Authentication.isClientOnly() ? null : oAuth2Authentication.getName());
        token.setClientId(oAuth2Authentication.getOAuth2Request().getClientId());
        token.setAuthenticationSerialize(oAuth2Authentication);

        OAuth2RefreshToken refreshToken = oAuth2AccessToken.getRefreshToken();
        token.setRefreshToken(refreshToken != null ? extractTokenKey(refreshToken.getValue()) : null);

        accessTokenRepository.save(token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String token) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByTokenId(extractTokenKey(token));
        return accessToken.map(AccessToken::getTokenDeserialize).orElse(null);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        Optional<AccessToken> accessToken = accessTokenRepository.findByTokenId(extractTokenKey(oAuth2AccessToken.getValue()));
        accessToken.ifPresent(at -> accessTokenRepository.delete(at));
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication oAuth2Authentication) {
        RefreshToken refresh = new RefreshToken();
        refresh.setTokenId(extractTokenKey(oAuth2RefreshToken.getValue()));
        refresh.setTokenSerialize(oAuth2RefreshToken);
        refresh.setAuthenticationSerialize(oAuth2Authentication);
        refreshTokenRepository.save(refresh);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String token) {
        Optional<RefreshToken> refresh = refreshTokenRepository.findByTokenId(extractTokenKey(token));
        return refresh.map(RefreshToken::getTokenDeserialize).orElse(null);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        Optional<RefreshToken> refresh = refreshTokenRepository.findByTokenId(extractTokenKey(oAuth2RefreshToken.getValue()));
        return refresh.map(RefreshToken::getAuthenticationDeserialize).orElse(null);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        Optional<RefreshToken> refresh = refreshTokenRepository.findByTokenId(extractTokenKey(oAuth2RefreshToken.getValue()));
        refresh.ifPresent(rt -> refreshTokenRepository.delete(rt));
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        Optional<AccessToken> token = accessTokenRepository.findByRefreshToken(extractTokenKey(oAuth2RefreshToken.getValue()));
        token.ifPresent(at -> accessTokenRepository.delete(at));
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication) {
        String authenticationId = authenticationKeyGenerator.extractKey(oAuth2Authentication);
        Optional<AccessToken> token = accessTokenRepository.findByAuthenticationId(authenticationId);

        if (token.isPresent()) {
            OAuth2AccessToken accessToken = token.get().getTokenDeserialize();
            if (accessToken != null && !authenticationId.equals(authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
                removeAccessToken(accessToken);
                storeAccessToken(accessToken, oAuth2Authentication);
            }
            return accessToken;
        }
        return null;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<>();
        List<AccessToken> result = accessTokenRepository.findByClientIdAndUserName(clientId, userName);
        result.forEach(e -> tokens.add(e.getTokenDeserialize()));
        return tokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<>();
        List<AccessToken> result = accessTokenRepository.findByClientId(clientId);
        result.forEach(e -> tokens.add(e.getTokenDeserialize()));
        return tokens;
    }

    private String extractTokenKey(String value) {
        if (value == null) {
            return null;
        } else {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] md5 = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
                return String.format("%032x", new BigInteger(1, md5));
            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException("MD5 algorithm not available.");
            }
        }
    }
}
