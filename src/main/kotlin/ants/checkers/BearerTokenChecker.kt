package ants.checkers

import ants.controllers.NetworkController
import ants.external.Token
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class BearerTokenChecker : AbstractChecker(setOf(NetworkController.FINDPATHS, NetworkController.STORE)) {
    companion object { private const val BEARER = "Bearer " }
    override fun checkRequest(request: HttpServletRequest): HttpStatus {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            val accessToken = authHeader.substring(7)
            if (accessToken == Token.getToken(request.contentLength)) return HttpStatus.OK
        }
        return HttpStatus.UNAUTHORIZED
    }
}
