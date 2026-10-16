package ants.external

import ants.models.Network
import com.fasterxml.jackson.annotation.JsonProperty

class Path() : Search() {

  @JsonProperty
  private var path: List<Int>? = emptyList()

  @JsonProperty
  private var direction: String? = ""

  constructor(search: Search, network: Network) : this() {
    from = search.getFrom()
    to = search.getTo()
    val findPath = network.findPath(from, to)
    path = findPath.map { it.getNodes().map { node -> node.id } }.orElse(emptyList())
    direction = findPath.map { it.getDirection().toString() }.orElse("")
  }

  fun isSameAs(other: Path) =
    from == other.from && to == other.to && path.toString() == other.path.toString()

  fun getPath() =
    path
}
