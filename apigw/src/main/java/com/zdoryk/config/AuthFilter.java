package com.zdoryk.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zdoryk.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;
    private final JWTUtil jwtUtil;

    public AuthFilter(WebClient.Builder webClientBuilder, JWTUtil jwtUtil) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
        this.jwtUtil = jwtUtil;
    }

    private final List<String> allowedUrls = List.of(
            "/api/v1/subscription",
            "/api/v1/search",
            "/api/v1/feedback",
            "/api/v1/url"
//            "/api/v1/info",
//            "/api/v1/card"
//            "/api/v1/data"
    );

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            String path = exchange.getRequest().getPath().toString();
            if (allowedUrls.stream().noneMatch(path::startsWith))
            {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new InvalidTokenException(HttpStatus.UNAUTHORIZED);
                }
                String authHeader = Objects.requireNonNull(
                                exchange
                                .getRequest()
                                .getHeaders()
                                .get(HttpHeaders.AUTHORIZATION))
                                .get(0);

                String[] parts = authHeader.split(" ");
                if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                    throw new InvalidTokenException(HttpStatus.NO_CONTENT);
                }
                try {
                   jwtUtil.validateTokenAndRetrieveClaim(parts[1]);
                   if(!jwtUtil.isTokenExpired(parts[1])){
                       throw new InvalidTokenException(HttpStatus.UNAUTHORIZED);
                   }
                } catch (JWTVerificationException ignored) {
                    throw new InvalidTokenException(HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }
}