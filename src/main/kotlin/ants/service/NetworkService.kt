package ants.service

import ants.common.NetworkException
import ants.external.Id
import ants.external.NetworkWithSearch
import ants.external.Paths
import ants.storage.NetworkStore
import ants.storage.PathStore
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class NetworkService(
    private val mapper: ObjectMapper,
    private val pathStore: PathStore,
    private val networkStore: NetworkStore
) {
    fun getPaths(id: String): Paths {
        val data = pathStore.findAllForHash(id)
        if (data.isEmpty()) throw NetworkException("No data found!", HttpStatus.NOT_FOUND.value(), listOf(id))
        return Paths(id, data.map { it.getObject(mapper) })
    }

    private fun getNetworkById(id: String): ants.external.Network? =
        networkStore.findAllForHash(id).firstOrNull()?.getObject(mapper)

    fun findPath(id: String, from: Int, to: Int): Paths {
        val data = getNetworkById(id)
            ?: throw NetworkException("No data found!", HttpStatus.NOT_FOUND.value(), listOf(id))
        val network = NetworkWithSearch(data)
        network.addSearch(from, to)
        val paths = findPaths(network, false)
        storePaths(id, paths.paths)
        return paths
    }

    @Transactional
    fun findPaths(data: NetworkWithSearch, store: Boolean): Paths {
        val network = data.getNetwork()
        val paths = data.getSearches().map { ants.external.Path(it, network) }
        val id = data.getId()
        if (store) {
            storePaths(id, paths)
            storeNetwork(data.getNetworkOnly())
        }
        return Paths(id, paths)
    }

    @Transactional
    fun storeNetwork(network: ants.external.Network): Id {
        val id = network.getId()
        if (getNetworkById(id) == null) networkStore.save(ants.storage.Network(mapper, id, network))
        return Id(id)
    }

    @Transactional
    private fun storePaths(id: String, paths: List<ants.external.Path>) {
        val existing = pathStore.findAllForHash(id).map { it.getObject(mapper) }
        paths.filter { path -> existing.none { it.isSameAs(path) } }
            .forEach { p -> pathStore.save(ants.storage.Path(mapper, id, p)) }
    }

    @Transactional
    fun delete(id: String): Boolean {
        val networks = networkStore.findAllForHash(id)
        if (networks.isEmpty()) return false
        networks.forEach { network ->
            network.getId()?.let { networkStore.deleteById(it) }
            pathStore.findAllForHash(network.getHash() ?: "")
                .mapNotNull { it.getId() }
                .let { pathStore.deleteAllById(it) }
        }
        return true
    }
}
