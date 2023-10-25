package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CorsFilter extends AbstractGatewayFilterFactory<CorsFilter.Config> {

    public CorsFilter(){super(Config.class);}

    @Override
    public GatewayFilter apply(CorsFilter.Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            log.info("CORS filter applied for request: {}", exchange.getRequest().getURI());
            log.info("Request method: {}", exchange.getRequest().getMethodValue());

            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, Set-Cookie, X-Auth-Token");
            headers.add("Access-Control-Max-Age", "3600");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequest().getMethodValue())) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            } else {
                return chain.filter(exchange);
            }
        }, Ordered.HIGHEST_PRECEDENCE); // 여기서 가장 높은 우선순위를 설정
    }

    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }
}
