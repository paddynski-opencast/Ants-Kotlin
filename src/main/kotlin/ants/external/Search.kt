package ants.external

import ants.models.Network
import com.fasterxml.jackson.annotation.JsonProperty

open class Search() {
    @JsonProperty @JvmField protected var from: Int = Network.INVALID
    @JsonProperty @JvmField protected var to: Int = Network.INVALID

    constructor(from: Int, to: Int) : this() { this.from = from; this.to = to }
    fun getFrom() = from
    fun getTo() = to
}
