package ants.external
import com.fasterxml.jackson.annotation.JsonProperty

class Paths(id: String, @JsonProperty val paths: List<Path>) : Id(id)
