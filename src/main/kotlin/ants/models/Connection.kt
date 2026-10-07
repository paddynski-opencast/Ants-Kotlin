package ants.models

import ants.common.Direction

data class Connection(
    val direction: Direction,
    val node: Node,
    val cost: Int
) : ants.common.Connection {
    fun isFor(direction: Direction) = this.direction == direction && node.isOpen()
    fun isValid() = node.isOpen()
}
