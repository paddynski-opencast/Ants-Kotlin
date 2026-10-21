package ants.external

import com.fasterxml.jackson.annotation.JsonProperty

open class Id() : IRequest, IResponse {

  @JsonProperty(required = true)
  var id: String = ""

  constructor(id: String) : this() {
    this.id = id
  }
}
