package ants.external

import com.fasterxml.jackson.annotation.JsonProperty

class IdWithSearch(id: String, from: Int, to: Int) : Id(id) {

  @JsonProperty(required = true)
  val searches = mutableListOf(Search(from, to))
}
