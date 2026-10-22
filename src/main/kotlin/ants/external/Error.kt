package ants.external

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Error(

  @JsonProperty
  val code: Int,

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  val ids: List<String>?,

  @JsonProperty
  val message: String?
) : IResponse
