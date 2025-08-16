package com.apnaStore.API_gateway.filter;

import com.apnaStore.API_gateway.enums.Role;
import com.apnaStore.API_gateway.messages.ErrorMessages;
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
    private RouteValidator validator;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthenticationFilter(RouteValidator validator, JwtUtils jwtUtils) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // check header contain token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, ErrorMessages.MISSING_AUTHORIZATION_HEADER, 401);
                }

                String token = getToken(exchange.getRequest());
                try {
                    if (!jwtUtils.validateToken(token)) {
                        log.error("Token expired...");
                        return onError(exchange, ErrorMessages.INVALID_OR_EXPIRED_JWT_TOKEN, 401);
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
                    ServerWebExchange finalExchange = exchange;
                    return authorizeRequest(role, exchange)
                            .flatMap(isAuthorized -> {
                                if (Boolean.TRUE.equals(isAuthorized)) {
                                    return chain.filter(finalExchange);
                                } else {
                                    return onError(finalExchange, ErrorMessages.ACCESS_DENIED_FOR_ROLE + " :: " + role.toUpperCase(), 403);
                                }
                            });
                } catch (Exception ex) {
                    log.error("Invalid JWT token!!");
                    return onError(exchange, ErrorMessages.INVALID_JWT_TOKEN, 401);
                }
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Boolean> authorizeRequest(String role, ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        log.info("Authorizing role: {}, method: {}, path: {}", role, method, path);

        if (role.equalsIgnoreCase(Role.ADMIN.getRole())) {
            return Mono.just(true);
        }

        if (role.equalsIgnoreCase(Role.CUSTOMER.getRole())) {
            if (path.startsWith("/api/users/address")) {
                return Mono.just(true);
            }

            if ((method.equalsIgnoreCase("DELETE") || method.equalsIgnoreCase("PUT")) && path.matches("/api/users/\\d+$")) {
                return Mono.just(true);
            }


            if (method.equalsIgnoreCase("GET") &&
                    (path.matches("/api/users/\\d+$")
                            || path.matches("/api/users/email/.+$")
                            || path.matches("/api/products/?")
                            || path.matches("/api/products/\\d+")
                            || path.matches("/api/products/search.*")
                            || path.matches("/api/products/category/\\d+")
                            || path.matches("/api/products/\\d+/details")
                            || path.matches("/api/products/categories/?")
                            || path.matches("/api/products/categories")
                            || path.matches("/api/products/categories/\\d+")
                            || path.matches("/api/products/\\d+/attributes")
                            // Inventory service
                            || path.matches("/api/inventory/?")
                            || path.matches("/api/inventory/\\?page=\\d+.*") // optional query params
                            || path.matches("/api/inventory/[A-Za-z0-9\\-]+$") // inventoryId
                            || path.matches("/api/inventory/product/\\d+")
                            || path.matches("/api/inventory/product/\\d+/total-stock")
                            // warehouse
                            || path.matches("/api/inventory/warehouse/?$")
                            || path.matches("/api/inventory/warehouse/[A-Za-z0-9\\-]+$"))) {
                return Mono.just(true);
            }

            return Mono.just(false);
        }

        return Mono.just(false);
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
