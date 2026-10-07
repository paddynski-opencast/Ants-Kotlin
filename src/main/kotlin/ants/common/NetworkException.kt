package ants.common

import ants.external.Error

class NetworkException(
    message: String,
    val code: Int,
    val ids: List<String>
) : RuntimeException(message) {
    fun getError() = Error(code, ids, message)
    companion object {
        const val ERROR_INVALID = -1
        const val ERROR_CREATE = -2
        const val ERROR_ABSENT = -3
        const val ERROR_PRESENT = -4
        const val ERROR_CONNECT = -5
    }
}
