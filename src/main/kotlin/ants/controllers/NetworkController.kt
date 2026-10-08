package ants.controllers

import ants.common.NetworkException
import ants.external.*
import ants.service.NetworkService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
open class NetworkController(
    private val service: NetworkService,
    private val mapper: ObjectMapper
) {
    companion object {
        const val FINDPATHS = "/findPaths"
        const val GETPATHS = "/getPaths"
        const val GETNET = "/get"
        const val FINDPATH = "/findPath"
        const val STORE = "/store"
        const val DEL = "/del"
        private val logger: Logger = LogManager.getLogger(NetworkController::class.java)
    }

    @RequestMapping(method = [GET], path = [GETNET])
    fun get(@RequestParam id: String): ResponseEntity<IResponse> {
        log(Id(id), false)
        return try {
            service.getNetworkById(id)
                .also { log(it, true) }
                .let { ResponseEntity.ok<IResponse>(it) }
        } catch (e: NetworkException) {
            error(e)
        }
    }

    @RequestMapping(method = [GET], path = [GETPATHS])
    fun getPaths(@RequestParam id: String): ResponseEntity<IResponse> {
        log(Id(id), false)
        return try {
            service.getPaths(id)
                .also { log(it, true) }
                .let { ResponseEntity.ok<IResponse>(it) }
            } catch (e: NetworkException) {
                error(e)
            }
    }

    fun findPaths(data: NetworkWithSearch, store: Boolean): ResponseEntity<IResponse> {
        log(data, false)
        return try {
            service.findPaths(data, store)
                .also { log(it, true) }
                .let { ResponseEntity.ok<IResponse>(it) }
            } catch (e: NetworkException) {
                error(e)
            }
    }

    @RequestMapping(method = [POST], path = [FINDPATHS])
    fun findPaths(@RequestBody data: NetworkWithSearch): ResponseEntity<IResponse> =
        findPaths(data, true)

    @RequestMapping(method = [POST], path = [STORE])
    fun store(@RequestBody data: Network): ResponseEntity<IResponse> {
        log(data, false)
        return try {
            service.storeNetwork(data)
                .also { log(it, true) }
                .let { ResponseEntity.ok<IResponse>(it) }
            } catch (e: NetworkException) {
                error(e)
            }
    }

    @RequestMapping(method = [GET], path = [FINDPATH])
    fun findPath(@RequestParam id: String, @RequestParam from: Int, @RequestParam to: Int): ResponseEntity<IResponse> {
        log(IdWithSearch(id, from, to), false)
        return try {
            service.findPath(id, from, to)
                .also { log(it, true) }
                .let { ResponseEntity.ok<IResponse>(it) }
            } catch (e: NetworkException) {
                error(e)
            }
    }

    @RequestMapping(method = [DELETE], path = [DEL])
    fun delete(@RequestParam id: String): ResponseEntity<IResponse> {
        log(Id(id), false)
        return try {
            Status(service.delete(id))
                .also { log(it, true) }
                .let { ResponseEntity.ok<IResponse>(it) }
            } catch (e: NetworkException) {
                error(e)
            }
    }

    private fun log(data: Loggable, isReturn: Boolean) {
        val message = if (isReturn) "Return = %s" else "Data   = %s"
        try {
            logger.info(message.format(mapper.writeValueAsString(data)))
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    private fun error(e: NetworkException): ResponseEntity<IResponse> {
        val error = e.getError()
        val code = when(error.code) {
            NOT_FOUND.value() -> error.code
            else -> BAD_REQUEST.value()
        }
        log(error, true)
        return ResponseEntity.status(code).body(error)
    }
}
