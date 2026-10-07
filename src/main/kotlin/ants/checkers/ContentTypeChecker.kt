package ants.checkers

import ants.controllers.NetworkController
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class ContentTypeChecker : AbstractChecker(setOf(NetworkController.FINDPATHS, NetworkController.STORE)) {
    override fun checkRequest(request: HttpServletRequest) =
        if (MediaType.APPLICATION_JSON.toString() == request.contentType) HttpStatus.OK
        else HttpStatus.UNSUPPORTED_MEDIA_TYPE
}
