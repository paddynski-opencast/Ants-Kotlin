package ants.external

import ants.common.Errors
import ants.common.NetworkException
import ants.common.NetworkException.Companion.ERROR_INVALID
import com.fasterxml.jackson.annotation.JsonProperty

class NetworkWithSearch : Network {
  @JsonProperty
  val searches = mutableListOf<Search>()

  constructor() : super()

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

  override fun validate() {
    super.validate()
    if (searches.isEmpty()) {
      throw NetworkException("Invalid Searches!", ERROR_INVALID, listOf())
    }
    val ids = nodes.map { it.id }
    searches.forEach { it.validate(ids) }
  }
}
