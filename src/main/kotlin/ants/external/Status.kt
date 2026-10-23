package ants.external

import com.fasterxml.jackson.annotation.JsonProperty

class Status : Response {

  @JsonProperty
  var status: Boolean = false

  constructor(status: Boolean) {
    this.status = status
  }
}
