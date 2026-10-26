package ants.external

import com.fasterxml.jackson.annotation.JsonProperty

class Status : Response {

  @JsonProperty
  var networks: Int = 0

  @JsonProperty
  var paths: Int = 0

  constructor(networks: Int, paths: Int) {
    this.networks = networks
    this.paths = paths
  }
}
