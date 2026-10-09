package ants.storage

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.annotation.Id

abstract class Entity<T> {
    @Id
    private var id: String? = null
    private var hash: String? = null
    private var json: String? = null

    protected constructor()

    constructor(mapper: ObjectMapper, hash: String, data: T) {
        this.hash = hash
        json = try { mapper.writeValueAsString(data) } catch (e: Exception) { throw RuntimeException(e) }
    }

    protected fun getObject(mapper: ObjectMapper, clazz: Class<*>): Any =
        try { mapper.readValue(json, clazz) } catch (e: Exception) { throw RuntimeException(e) }

    fun getId() = id
    fun getHash() = hash
    fun getJson() = json
}
