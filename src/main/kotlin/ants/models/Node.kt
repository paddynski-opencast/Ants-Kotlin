package ants.models

import ants.common.Direction

class Node internal constructor(
    val id: Int,
    private var closed: Boolean
) : ants.common.Node() {
    private val connections = linkedSetOf<Connection>()

    internal fun getConnections(direction: Direction) =
        connections.filter { it.isFor(direction) }.toMutableList()

    internal fun connect(direction: Direction, node: Node, cost: Int) {
        if (cost >= 0) connections.add(Connection(direction, node, cost))
    }

    internal fun setClosed(closed: Boolean) {
        this.closed = closed
    }

    internal fun isOpen() = !closed

    override fun toString() = "[$id]"

    internal fun getData() = ants.external.Node(id, closed)

    internal fun getConnectionData() =
        connections.asSequence().map { ants.external.Connection(id, it.node.id, it.direction, it.cost) }

    fun getConnections() = connections.toSet()
    fun isClosed() = closed
}
