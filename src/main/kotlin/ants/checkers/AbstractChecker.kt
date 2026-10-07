package ants.checkers

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

abstract class AbstractChecker(private val paths: Set<String> = emptySet()) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI in paths) {
            val status = checkRequest(request)
            if (status != HttpStatus.OK) {
                response.status = status.value()
                return
            }
        }
        filterChain.doFilter(request, response)
    }
    protected abstract fun checkRequest(request: HttpServletRequest): HttpStatus
}
