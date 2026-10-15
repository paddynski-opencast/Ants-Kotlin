package ants.external

import ants.common.NetworkException
import ants.common.NetworkException.Companion.ERROR_INVALID
import com.fasterxml.jackson.annotation.JsonProperty

open class Search() {
  @JsonProperty
  @JvmField
  protected var from: Int = ants.common.Network.INVALID

  @JsonProperty
  @JvmField
  protected var to: Int = ants.common.Network.INVALID

  constructor(from: Int, to: Int) : this() {
    this.from = from; this.to = to
  }

  fun getFrom() = from

  fun getTo() = to

  fun validate(ids: List<Int>) {
    val invalidIds = listOf(from, to).filter { !ids.contains(it) }
    if (invalidIds.isNotEmpty()) {
      throw NetworkException("Invalid Search!", ERROR_INVALID, invalidIds.map { it.toString() })
    }
  }
}
