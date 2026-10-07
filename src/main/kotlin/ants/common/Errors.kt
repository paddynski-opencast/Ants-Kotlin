package ants.common

object Errors {
    private const val ERROR_ALREADY_EXISTS = "%s already exists! - [%d]"
    private const val ERROR_DOES_NOT_EXIST = "%s does not exist! - [%d]"
    private const val ERROR_CANNOT_CREATE = "Cannot create %s!"

    fun invalidState() = error(NetworkException.ERROR_INVALID, "Invalid state!")

    fun error(code: Int, message: String) = error(message, code, emptyList())

    fun error(code: Int, message: String, id: Int) = error(message, code, listOf(id))

    fun alreadyExists(clazz: Class<*>, id: Int) =
        error(ERROR_ALREADY_EXISTS.format(clazz.simpleName, id), NetworkException.ERROR_PRESENT, listOf(id))

    fun doesNotExist(clazz: Class<*>, id: Int) =
        error(ERROR_DOES_NOT_EXIST.format(clazz.simpleName, id), NetworkException.ERROR_ABSENT, listOf(id))

    fun cannotCreate(clazz: Class<*>) =
        error(ERROR_CANNOT_CREATE.format(clazz.simpleName), NetworkException.ERROR_CREATE, emptyList())

    private fun error(message: String, code: Int, ids: List<Int>) {
        throw NetworkException(message, code, ids.map(Int::toString))
    }
}
