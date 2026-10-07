package ants.external

import ants.common.Errors
import ants.common.NetworkException
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class NetworkWithSearch : Network {
    @JsonProperty
    val searches = mutableListOf<Search>()

    constructor() : super()
    constructor(name: String) : super(name)
    constructor(network: ants.models.Network) : super(network)
    constructor(network: Network) : super(network.name) {
        nodes = network.nodes
        connections = network.connections
    }

    fun addSearch(from: Int, to: Int) {
        if (nodeExists(from) && nodeExists(to)) {
            searches.add(Search(from, to))
            return
        }
        Errors.error(NetworkException.ERROR_INVALID, "Invalid search!")
    }
}
