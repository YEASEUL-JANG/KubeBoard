package com.miniproject.kubeBoard.config

import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CorsFilter : Filter {

    override fun init(filterConfig: FilterConfig) {
        // Nothing to initialize
    }

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        response.apply {
            setHeader("Access-Control-Allow-Origin", "*")
            setHeader("Access-Control-Allow-Credentials", "true")
            setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS")
            setHeader(
                    "Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, Content-Type, Accept, Authorization, Set-Cookie, X-Auth-Token"
            )
            setHeader("Access-Control-Max-Age", "3600")
        }

        if ("OPTIONS".equals(request.method, ignoreCase = true)) {
            response.status = HttpServletResponse.SC_OK
        } else {
            chain.doFilter(req, res)
        }
    }

    override fun destroy() {
        // Nothing to destroy
    }
}