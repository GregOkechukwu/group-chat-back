package com.groupchatback.util;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;

import com.groupchatback.aws.CognitoIdentityProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;

public class TokenUtil {

    public static String[] manageTokens(String accessTknBase64, String refreshTknBase64) {
        String[] tokens = new String[2];

        if (accessTknBase64 == null || refreshTknBase64 == null) {
            return tokens;
        }

        String decodedUserId, accessTkn, refreshTkn;
        decodedUserId = accessTkn = refreshTkn = null;

        try {
            int errCount = 0;

            accessTkn = decodeBase64(accessTknBase64);
            refreshTkn = decodeBase64(refreshTknBase64);
            decodedUserId = decodeAndGetUserId(accessTkn);

            while (decodedUserId == null && errCount < 2) {
                accessTkn = refreshAccessToken(refreshTkn);
                decodedUserId = decodeAndGetUserId(accessTkn);
                errCount++;
            }

            if (errCount == 2) {
                throw new IllegalArgumentException();
            }

        } catch (Exception e) {
            System.out.println(e);
            return tokens;
        }

        tokens[0] = encodeBase64(accessTkn);
        tokens[1] = decodedUserId;

        return tokens;
    }

    public static String refreshAccessToken(String refreshTkn) throws NotAuthorizedException {
        if (refreshTkn == null) {
            return refreshTkn;
        }

        return CognitoIdentityProvider.refreshToken(refreshTkn);
    }

    public static String encodeBase64(String str) {
        if (str == null) {
            return str;
        }

        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String decodeBase64(String str) throws IllegalArgumentException {
        if (str == null) {
            return str;
        }

        byte[] bytes = Base64.getDecoder().decode(str.getBytes());
        return new String(bytes);
    }

    public static boolean isValid(String accessTknBase64) {
        String userId = null;

        try {
            String accessTkn = decodeBase64(accessTknBase64);
            userId = decodeAndGetUserId(accessTkn);
        } catch (Exception e) {
            return false;
        }

        return userId != null;
    }

    private static String decodeAndGetUserId(String accessTkn) throws JwkException, MalformedURLException {
        if (accessTkn == null) {
            return accessTkn;
        }

        String[] splitToken = accessTkn.split("\\.");

        final String base64EncodedHeader = splitToken[0];
        final String decodedHeader = decodeBase64(base64EncodedHeader);
        final String kid = getProperty(getJSON(decodedHeader), "kid");
        final String jwkUrl = EnvConfigUtil.getProperty("JWK_URL");

        final JwkProvider provider = new UrlJwkProvider(new URL(jwkUrl));
        final Jwk jwk = provider.get(kid);
        final PublicKey publicKey = jwk.getPublicKey();
        final RsaVerifier rsaVerifier = new RsaVerifier((RSAPublicKey) publicKey);

        final Jwt jwt = JwtHelper.decodeAndVerify(accessTkn, rsaVerifier);

        return isValid(jwt) ? getUserId(jwt) : null;
    }

    private static boolean isValid(Jwt jwt) {
        final JSONObject claimsObj = getJSON(jwt.getClaims());

        final Long expireTimeInSec = Long.parseLong(getProperty(claimsObj, "exp"));
        final Long timeNowInSec = new Date().getTime() / 1000;
        final String clientId = getProperty(claimsObj, "client_id");
        final String tokenUse = getProperty(claimsObj, "token_use");

        final boolean isExpired = expireTimeInSec <= timeNowInSec;
        final boolean usesValidClientId = StringUtils.equals(clientId, EnvConfigUtil.getProperty("CLIENT_ID"));
        final boolean usesValidTokenType = StringUtils.equals(tokenUse, EnvConfigUtil.getProperty("TKN_USE"));

        return !isExpired && usesValidClientId && usesValidTokenType;
    }

    private static String getUserId (Jwt jwt) {
        JSONObject claimsObj = getJSON(jwt.getClaims());
        return getProperty(claimsObj, "username");
    }

    private static JSONObject getJSON(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
    }

    private static String getProperty(JSONObject obj, String property) {
        try {
            return obj.getString(property);
        } catch (JSONException e) {
            return null;
        }
    }
}
