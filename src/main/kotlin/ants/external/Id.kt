package ants.external

import com.fasterxml.jackson.annotation.JsonProperty

open class Id() : Request, Response {

  @JsonProperty(required = true)
  var id: String = ""

  constructor(id: String) : this() {
    this.id = id
  }
}
