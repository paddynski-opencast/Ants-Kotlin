package ants.external

import ants.models.Network
import com.fasterxml.jackson.annotation.JsonProperty

class Path() {
  companion object {
    private val NO_PATH = emptyList<Int>()
    private const val NO_DIRECTION = ""
    private const val NO_COST = -1
  }

  @JsonProperty
  private var search: Search = Search()

  @JsonProperty
  private var path: List<Int>? = NO_PATH

  @JsonProperty
  private var direction: String? = NO_DIRECTION

  @JsonProperty
  private var cost: Int? = NO_COST

  constructor(search: Search, network: Network) : this() {
    this.search = search
    val findPath = network.findPath(search.getFrom(), search.getTo(), search.getOptimization())
    path = findPath.map { it.getNodes().map { node -> node.id } }.orElse(NO_PATH)
    direction = findPath.map { it.getDirection().toString() }.orElse(NO_DIRECTION)
    cost = findPath.map { it.getCost() }.orElse(NO_COST)
  }

  fun isSameAs(other: Path) =
    search.isSameAs(other.search) && path.toString() == other.path.toString()

  fun getPath() =
    path
}
