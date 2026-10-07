package ants.external
import com.fasterxml.jackson.annotation.JsonProperty
class Status(@JsonProperty val status: Boolean) : IResponse
