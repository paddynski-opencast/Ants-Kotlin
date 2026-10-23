package ants.controllers

import ants.external.Response
import ants.external.Network
import ants.external.NetworkWithSearch
import ants.service.NetworkService
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@Hidden
@RestController
class PageController(
  private val mapper: ObjectMapper,
  private val controller: NetworkController,
  private val service: NetworkService
) : AbstractController() {

  @RequestMapping(method = [RequestMethod.GET], path = ["/"])
  fun index(@RequestParam reset: Optional<String>): String? {
    return try {
      val network = Network("Test")
      service.delete(network.id())
      if (reset.isEmpty) {
        network.addNode(0)
        network.addNode(1)
        network.connect(0, 1, 0, -1)
      }
      val id = service.storeNetwork(network)
      show(id.id)
    } catch (e: Exception) {
      error("", e)
    }
  }

  @RequestMapping(method = [RequestMethod.GET], path = ["/show/{id}"])
  fun show(@PathVariable id: String): String? {
    return try {
      val response: Response? = controller.get(id).body
      val max = service.getNetworkById(id).nodes.size - 1
      getContents(
        "/index.html", mapOf(
          "id" to id,
          "network" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response),
          "max" to max.toString()
        )
      )
    } catch (e: Exception) {
      error(id, e)
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
      error(id, e)
    }
  }

  @RequestMapping(method = [RequestMethod.POST], path = ["/connect"])
  fun connect(@RequestParam id: String, @RequestParam from: String, @RequestParam to: String): String? {
    return try {
      val network = service.getNetworkById(id)
      network.connect(from.toInt(), to.toInt(), 0, -1)
      service.delete(id)
      show(service.storeNetwork(network).id)
    } catch (e: Exception) {
      error(id, e)
    }
  }

  @RequestMapping(method = [RequestMethod.POST], path = ["/test"])
  fun test(@RequestParam id: String, @RequestParam from: String, @RequestParam to: String): String? {
    return try {
      val request = NetworkWithSearch(service.getNetworkById(id))
      request.addSearch(from.toInt(), to.toInt())
      val response: Response? = controller.findPaths(request).body
      getContents(
        "/test.html", mapOf(
          "id" to id,
          "request" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request),
          "response" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response)
        )
      )
    } catch (e: Exception) {
      error(id, e)
    }
  }

  private fun error(id: String, e: Exception): String? =
    getContents(
      "/error.html", mapOf(
        "id" to id,
        "error" to e.message.orEmpty()
      )
    )
}
