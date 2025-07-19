package com.apnaStore.API_gateway.filter;

import com.apnaStore.API_gateway.util.JwtUtils;
import com.apnaStore.API_gateway.validator.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private final RouteValidator validator;

    @Autowired
    private final JwtUtils jwtUtils;

    public AuthenticationFilter(RouteValidator validator, JwtUtils jwtUtils) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.info("Gateway Auth Filter..");
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // check header contain token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing Authorization header", 401);
                }

                String token = getToken(exchange.getRequest());
                try {
                    if (!jwtUtils.validateToken(token)) {
                        log.error("Token expired...");
                        return onError(exchange, "Invalid or expired JWT token", 401);
                    }

                    log.info("Token validated.....");
                    String role = jwtUtils.extractRole(token);
                    String userName = jwtUtils.extractUserName(token);
                    log.info("Token validated: user={}, role={}", userName, role);

                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-Username", userName)
                            .header("X-Role", role)
                            .build();

                    exchange = exchange.mutate().request(modifiedRequest).build();
                } catch (Exception ex) {
                    log.error("Invalid JWT token!!");
                    return onError(exchange, "Invalid JWT token", 401);
                }
            }
            return chain.filter(exchange);
        });
    }

    private String getToken(ServerHttpRequest request) {
        String authHeader = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
            log.info("Extracted Token :: {}", authHeader);
        }
        return authHeader;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, int statusCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(statusCode));
        response.getHeaders().add("Content-Type", "application/json");

        String path = exchange.getRequest().getPath().toString();
        String errorJson = String.format(
                "{\"error\": \"%s\", \"end-point\": \"%s\"}",
                message,
                path
        );
        DataBuffer buffer = response.bufferFactory().wrap(errorJson.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {
    }
}
