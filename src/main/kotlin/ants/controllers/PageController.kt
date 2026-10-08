package ants.controllers

import ants.external.IResponse
import ants.external.Id
import ants.external.Network
import ants.external.NetworkWithSearch
import ants.service.NetworkService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.*

@RestController
open class PageController(
    private val mapper: ObjectMapper,
    private val controller: NetworkController,
    private val service: NetworkService
) : AbstractController() {

    @RequestMapping(method = [RequestMethod.GET], path = ["/"])
    fun index(): String? {
        return try {
            val network = Network("Test")
            service.delete(network.id())
            val id = service.storeNetwork(network)
            show(id.id)
        } catch (e: Exception) {
            e.message
        }
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/show/{id}"])
    fun show(@PathVariable id: String): String? {
        return try {
            val response: IResponse? = controller.get(id).body
            val max = service.getNetworkById(id).nodes.size - 1
            getContents("/index.html", mapOf(
                "id" to id,
                "network" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response),
                "max" to max.toString()
            ))
        } catch (e: Exception) {
            e.message
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/add"])
    fun add(@RequestParam id: String): String? {
        return try {
            val network = service.getNetworkById(id)
            service.delete(id)
            network.addNode(network.nodes.size)
            show(service.storeNetwork(network).id)
        } catch (e: Exception) {
            e.message
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/connect"])
    fun connect(@RequestParam id: String, @RequestParam from: String, @RequestParam to: String): String? {
        return try {
            val nodes = listOf(from.toInt(), to.toInt())
            val network = service.getNetworkById(id)
            service.delete(id)
            network.connect(nodes.min(), nodes.max())
            show(service.storeNetwork(network).id)
        } catch (e: Exception) {
            e.message
        }
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/test"])
    fun test(@RequestParam id: String, @RequestParam from: String, @RequestParam to: String): String? {
        return try {
            val request = NetworkWithSearch(service.getNetworkById(id))
            request.addSearch(from.toInt(), to.toInt())
            val response: IResponse? = controller.findPaths(request, false).body
            getContents("/test.html", mapOf(
                "id" to id,
                "request" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request),
                "response" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response)
            ))
        } catch (e: Exception) {
            e.message
        }
    }
}
