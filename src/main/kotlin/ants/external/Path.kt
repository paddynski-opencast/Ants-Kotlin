package ants.external

import ants.models.Network
import com.fasterxml.jackson.annotation.JsonProperty

class Path() : Search() {
    @JsonProperty
    private var path: List<Int> = emptyList()

    constructor(search: Search, network: Network) : this() {
        from = search.getFrom()
        to = search.getTo()
        path = network.findPath(from, to).map { it.getNodes().map { node -> node.id } }.orElse(emptyList())
    }

    fun isSameAs(other: Path) = from == other.from && to == other.to && path.toString() == other.path.toString()
    fun getPath() = path
}
