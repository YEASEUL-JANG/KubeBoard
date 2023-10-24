package com.example.userservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfiguration {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration(){

        //circuitBreakerConfig and timeLimiterConfig objects
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                //서킷브레이커를 열지 결정하는 실패율 (default : 50)
                .failureRateThreshold(4)
                //서킷브레이커의 open 상태 유지 지속시간, 이 시간이후엔 half-open상태 (default: 60초)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                // 서킷브레이커가 닫힐 때 호출했었던 결과값을 카운트기반/ 시간기반으로 할것인지 결정
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(2)
                .build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                //답을 공급하는 서비스가 4초동안 답이 없을 시 오류로 간주하고 서킷브레이커를 닫는다.
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configureDefault(id-> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }
}
