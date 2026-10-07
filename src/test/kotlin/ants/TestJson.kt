package ants

import ants.common.NetworkException
import ants.controllers.NetworkController
import ants.external.*
import ants.models.Network
import ants.service.NetworkService
import ants.storage.NetworkStore
import ants.storage.PathStore
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.lang.reflect.Proxy
import java.util.UUID
import kotlin.collections.ArrayList

private class FakeStores {
    val paths = ArrayList<ants.storage.Path>()
    val networks = ArrayList<ants.storage.Network>()

    @Suppress("UNCHECKED_CAST")
    val pathStore: PathStore = Proxy.newProxyInstance(
        PathStore::class.java.classLoader, arrayOf(PathStore::class.java)
    ) { _, method, args ->
        when (method.name) {
            "findAllForHash" -> paths.filter { it.getHash() == args!![0] }
            "save" -> (args!![0] as ants.storage.Path).also { paths.add(it) }
            "saveAll" -> (args!![0] as Iterable<ants.storage.Path>).toList().also { paths.addAll(it) }
            "count" -> paths.size.toLong()
            "findAll" -> paths.toList()
            else -> defaultValue(method.returnType)
        }
    } as PathStore

    @Suppress("UNCHECKED_CAST")
    val networkStore: NetworkStore = Proxy.newProxyInstance(
        NetworkStore::class.java.classLoader, arrayOf(NetworkStore::class.java)
    ) { _, method, args ->
        when (method.name) {
            "findAllForHash" -> networks.filter { it.getHash() == args!![0] }
            "save" -> (args!![0] as ants.storage.Network).also { networks.add(it) }
            "saveAll" -> (args!![0] as Iterable<ants.storage.Network>).toList().also { networks.addAll(it) }
            "count" -> networks.size.toLong()
            "findAll" -> networks.toList()
            else -> defaultValue(method.returnType)
        }
    } as NetworkStore

    fun pathCount(hash: String) = paths.count { it.getHash() == hash }
    fun networkCount(hash: String) = networks.count { it.getHash() == hash }

    private fun defaultValue(type: Class<*>) = when {
        type == Boolean::class.javaPrimitiveType -> false
        type == Long::class.javaPrimitiveType -> 0L
        type == Int::class.javaPrimitiveType -> 0
        type == Void.TYPE -> null
        type.isAssignableFrom(List::class.java) -> emptyList<Any>()
        else -> null
    }
}

class TestJson {
    private val mapper = ObjectMapper()
    private val stores = FakeStores()
    private val controller = NetworkController(
        NetworkService(mapper, stores.pathStore, stores.networkStore), mapper
    )

    private fun network(): Network = Network().apply {
        addNode(0); addNode(1); addNode(2); addNode(3); addNode(4); addNode(10)
        connect(0, 1); connect(1, 2); connect(1, 3); connect(4, 1)
    }

    @Test fun testData() {
        val expected = ants.external.Network("")
        expected.addNode(0); expected.addNode(1); expected.addNode(2); expected.addNode(3); expected.addNode(4); expected.addNode(10)
        expected.connect(0, 1); expected.connect(1, 2); expected.connect(1, 3); expected.connect(4, 1)
        assertEquals(expected, ants.external.Network(network()))
    }

    @Test fun testJson() {
        val expected = ants.external.Network(network())
        val json = mapper.writeValueAsString(expected)
        assertEquals(expected, mapper.readValue(json, ants.external.Network::class.java))
    }

    @Test fun testControllerFindPathsOk() {
        val data = NetworkWithSearch(network())
        data.addSearch(0, 2)
        repeat(5) {
            val response = controller.findPaths(data)
            assertEquals(HttpStatus.OK.value(), response.statusCode.value())
            val paths = (response.body as Paths).paths
            assertEquals(1, paths.size)
            assertEquals(listOf(0, 1, 2), paths[0].getPath())
        }
        val id = data.id()
        assertEquals(1, stores.networkCount(id))
        assertEquals(1, stores.pathCount(id))
    }

    @Test fun testControllerFindPathsError() {
        val data = NetworkWithSearch(network())
        data.getSearches().add(Search(0, 12))
        val response = controller.findPaths(data)
        assertEquals(400, response.statusCode.value())
        val error = response.body as Error
        assertEquals(NetworkException.ERROR_ABSENT, error.code)
        assertEquals(listOf("12"), error.ids)
    }

    @Test fun testControllerGetNoData() {
        val response = controller.getPaths(UUID.randomUUID().toString())
        assertEquals(404, response.statusCode.value())
    }

    @Test fun testControllerGetHasData() {
        val data = NetworkWithSearch(network())
        data.addSearch(0, 2)
        val response = controller.findPaths(data)
        val id = (response.body as Paths).id
        val getResponse = controller.getPaths(id)
        assertEquals(200, getResponse.statusCode.value())
        assertTrue((getResponse.body as Paths).paths.isNotEmpty())
        assertEquals(1, stores.networkCount(id))
    }

    @Test fun testControllerFindPath() {
        val response = controller.store(ants.external.Network(network()))
        assertEquals(200, response.statusCode.value())
        val id = (response.body as Id).id
        val pathResponse = controller.findPath(id, 0, 2)
        assertEquals(200, pathResponse.statusCode.value())
        val paths = (pathResponse.body as Paths).paths
        assertEquals(1, paths.size)
        assertEquals(listOf(0, 1, 2), paths[0].getPath())
        assertEquals(1, stores.networkCount(id))
        assertEquals(1, stores.pathCount(id))
    }
}
