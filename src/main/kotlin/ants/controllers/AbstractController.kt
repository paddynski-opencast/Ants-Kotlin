package ants.controllers

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Objects

abstract class AbstractController {
    protected fun getContents(name: String) = getContents(name, emptyMap())

    protected fun getContents(name: String, vars: Map<String, String>): String? {
        return try {
            val reader = BufferedReader(
                InputStreamReader(Objects.requireNonNull(javaClass.getResourceAsStream(name)))
            )
            replaceVarsWithValues(reader.lines().collect(java.util.stream.Collectors.joining("\n")), vars)
        } catch (e: Exception) {
            e.message
        }
    }

    private fun replaceVarsWithValues(template: String, vars: Map<String, String>): String {
        if (vars.isEmpty()) return template
        var content = template
        vars.forEach { (key, value) -> content = content.replace("\\{$key\\}".toRegex(), value) }
        return content
    }
}
