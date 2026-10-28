package ants.external

import ants.models.Network
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Path() {

  @JsonProperty
  private var search: Search = Search()

  @JsonProperty
  private var path: List<Int>? = null

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private var direction: String? = null

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private var cost: Int? = null

  constructor(search: Search, network: Network) : this() {
    this.search = search
    val findPath = network.findPath(search.getFrom(), search.getTo(), search.getOptimization())
    path = findPath.map { it.getNodes().map { node -> node.id } }.orElse(emptyList())
    direction = findPath.map { it.getDirection()?.toString() }.orElse(null)
    cost = findPath.map { it.getCost() }.orElse(null)
  }

  fun isSameAs(other: Path) =
    search.isSameAs(other.search) && path.toString() == other.path.toString()

  fun getPath() =
    path
}
