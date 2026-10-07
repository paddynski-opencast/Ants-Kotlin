package ants.controllers

import ants.external.IResponse
import ants.external.NetworkWithSearch
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.*

@RestController
open class PageController(
    private val mapper: ObjectMapper,
    private val controller: NetworkController
) : AbstractController() {

    @RequestMapping(method = [RequestMethod.GET], path = ["/"])
    fun index() = getContents("/index.html")

    @RequestMapping(method = [RequestMethod.POST], path = ["/test"])
    fun test(@RequestParam from: String, @RequestParam to: String): String? {
        val request = createNetwork()
        return try {
            request.addSearch(from.toInt(), to.toInt())
            val response: IResponse? = controller.findPaths(request, false).body
            getContents("/test.html", mapOf(
                "request" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request),
                "response" to mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response)
            ))
        } catch (e: Exception) {
            e.message
        }
    }

    private fun createNetwork() = NetworkWithSearch("Test").apply {
        addNode(0); addNode(1); addNode(2); addNode(3); addNode(4)
        connect(0, 1, 0, -1)
        connect(1, 2)
        connect(0, 3, -1, 0)
        connect(3, 2)
        connect(4, 3)
    }
}
