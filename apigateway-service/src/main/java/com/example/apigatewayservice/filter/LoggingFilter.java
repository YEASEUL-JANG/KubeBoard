package com.example.apigatewayservice.filter;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();

            if(config.isRequestLogger()){
                log.info("[{}] Request start ",config.getMsName());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isResponseLogger()){
                    log.info("[{}] Response end -> {}",config.getMsName(), response.getStatusCode());
                }
            }));
        };
    }

    @Data
    public static class Config{
        private String msName;
        private boolean requestLogger;
        private boolean responseLogger;
    }
}