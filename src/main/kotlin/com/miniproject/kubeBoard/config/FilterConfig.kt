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
        registrationBean.addUrlPatterns("/v1/*") // or other API patterns
        return registrationBean
    }
}