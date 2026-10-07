package ants.external
import ants.common.Direction
import com.fasterxml.jackson.annotation.JsonProperty

class Connection() : ants.common.Connection {
    @JsonProperty var from: Int = 0
    @JsonProperty var to: Int = 0
    @JsonProperty var direction: Direction = Direction.A
    @JsonProperty var cost: Int = 0

    constructor(from: Int, to: Int, direction: Direction, cost: Int?) : this() {
        this.from = from; this.to = to; this.direction = direction; this.cost = cost ?: 0
    }

    override fun equals(other: Any?) =
        other is Connection && from == other.from && to == other.to && direction == other.direction && cost == other.cost
    override fun hashCode() = java.util.Objects.hash(from, to, direction, cost)
    fun hash() = "%d-%d-%s-%d".format(from, to, if (direction == Direction.A) "A" else "B", cost)
}
