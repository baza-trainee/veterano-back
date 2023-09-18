package com.zdoryk.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secret;

    public void validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Spring")
                .build();

        DecodedJWT jwt = verifier.verify(token);

        jwt.getClaim("email").asString();
    }

    public boolean isTokenExpired(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("User details")
                    .withIssuer("Spring")
                    .build();

            DecodedJWT jwt = verifier.verify(token);

            Claim expClaim = jwt.getClaim("exp");

            if (!expClaim.isNull()) {

                long expirationTimestamp = expClaim.asLong() * 1000L;

                long currentTimeMillis = System.currentTimeMillis();

                return expirationTimestamp > currentTimeMillis;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
