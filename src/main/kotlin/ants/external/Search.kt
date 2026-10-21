package ants.external

import ants.common.NetworkException
import ants.common.NetworkException.Companion.ERROR_INVALID
import ants.common.Optimization
import com.fasterxml.jackson.annotation.JsonProperty

open class Search() {

  @JsonProperty
  private var from: Int = ants.common.Network.INVALID

  @JsonProperty
  private var to: Int = ants.common.Network.INVALID

  @JsonProperty
  private var optimization: Optimization = Optimization.C

  constructor(from: Int, to: Int, optimization: Optimization = Optimization.C) : this() {
    this.from = from
    this.to = to
    this.optimization = optimization
  }

  fun getFrom() =
    from

  fun getTo() =
    to

  fun getOptimization() =
    optimization

  fun validate(ids: List<Int>) {
    val invalidIds = listOf(from, to).filter { !ids.contains(it) }
    if (invalidIds.isNotEmpty()) {
      throw NetworkException("Invalid Search!", ERROR_INVALID, invalidIds.map { it.toString() })
    }
  }

  fun isSameAs(other: Search) =
    from == other.from && to == other.to && optimization == other.optimization
}
