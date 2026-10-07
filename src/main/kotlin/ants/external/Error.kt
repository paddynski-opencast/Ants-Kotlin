package ants.external

import com.fasterxml.jackson.annotation.JsonProperty

data class Error(
    @JsonProperty val code: Int,
    @JsonProperty val ids: List<String>,
    @JsonProperty val message: String?
) : IResponse
