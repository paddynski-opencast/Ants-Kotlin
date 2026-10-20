package ants.external

import ants.models.Network
import com.fasterxml.jackson.annotation.JsonProperty

class Path() : Search() {
  companion object {
    private val NO_PATH = emptyList<Int>()
    private val NO_DIRECTION = ""
    private val NO_COST = -1
  }

  @JsonProperty
  private var path: List<Int>? = NO_PATH

  @JsonProperty
  private var direction: String? = NO_DIRECTION

  @JsonProperty
  private var cost: Int? = NO_COST

  constructor(search: Search, network: Network) : this() {
    from = search.getFrom()
    to = search.getTo()
    val findPath = network.findPath(from, to)
    path = findPath.map { it.getNodes().map { node -> node.id } }.orElse(NO_PATH)
    direction = findPath.map { it.getDirection().toString() }.orElse(NO_DIRECTION)
    cost = findPath.map { it.getCost() }.orElse(NO_COST)
  }

  fun isSameAs(other: Path) =
    from == other.from && to == other.to && path.toString() == other.path.toString()

  fun getPath() =
    path
}
