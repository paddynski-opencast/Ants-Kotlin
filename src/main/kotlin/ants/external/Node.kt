package ants.external

import com.fasterxml.jackson.annotation.JsonProperty

class Node() : ants.common.Node() {
    @JsonProperty @JvmField var id: Int = 0
    @JsonProperty var closed: Boolean = false

    constructor(id: Int, closed: Boolean) : this() {
        this.id = id
        this.closed = closed
    }

    override fun equals(other: Any?) = other is Node && id == other.id

    override fun hashCode() = id

    fun hash() = "$id-${if (closed) 1 else 0}"
}
