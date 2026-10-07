package ants.external
import java.util.Base64
object Token {
    private const val TOKEN = "e80cebd5-8660-4284-8883-a50e806b0c7f-%s"
    @JvmStatic fun getToken(length: Int) =
        TOKEN.format(Base64.getEncoder().encodeToString(length.toString().toByteArray()))
}
