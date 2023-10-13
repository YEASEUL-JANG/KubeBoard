package com.miniproject.kubeBoard.config
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    @Bean
    fun loggingFilter(): FilterRegistrationBean<CorsFilter> {
        val registrationBean = FilterRegistrationBean<CorsFilter>()
        registrationBean.filter = CorsFilter()
        registrationBean.addUrlPatterns("/v1/*") // /v1/* 패턴의 url 에 CorsFilter를 적용하도록 설정
        return registrationBean
    }
}