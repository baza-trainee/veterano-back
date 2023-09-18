package com.zdoryk.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zdoryk.auth.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secret;

    @Value("${security.jwt.token.expiration}")
    private long jwtExpiration;

    @Value("${security.jwt.token.refresh-token.expiration}")
    private long refreshExpiration;


    private String buildToken(String email, long jwtExpiration) {

        return JWT.create()
                .withSubject("User details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("Spring")
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
                .sign(Algorithm.HMAC256(secret));

    }


    public String generateRefreshToken(String email) {
        return buildToken(email, refreshExpiration);
    }


    public String generateToken(String email) {
        return buildToken(email, jwtExpiration);
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Spring")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
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

                return currentTimeMillis > expirationTimestamp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
