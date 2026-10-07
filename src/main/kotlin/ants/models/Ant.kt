package ants.models

import ants.common.Direction

internal class Ant : Thread {
    private class Nodes(vararg initial: Node) {
        val nodes = initial.toMutableList()
        fun addNode(node: Node) = nodes.add(node)
        fun getLast() = nodes.lastOrNull()
        fun copyAllButLast() = Nodes().also { if (nodes.size > 1) it.nodes.addAll(nodes.dropLast(1)) }
        fun contains(node: Node) = nodes.contains(node)
    }

    private val network: Network
    private val pathNodes: Nodes
    val destination: Node
    val direction: Direction
    var cost: Int = 0
        private set

    constructor(network: Network, start: Node, destination: Node, direction: Direction) {
        this.network = network
        this.pathNodes = Nodes(start)
        this.destination = destination
        this.direction = direction
        network.addAnt()
        start()
    }

    private constructor(parent: Ant, connection: Connection) {
        network = parent.network
        pathNodes = parent.pathNodes.copyAllButLast().also { it.addNode(connection.node) }
        destination = parent.destination
        direction = parent.direction
        cost = parent.cost + connection.cost
        network.addAnt()
        start()
    }

    override fun run() {
        var next = pathNodes.getLast()
        while (next != null && next !== destination) {
            val connections = next.getConnections(direction)
            if (connections.isEmpty()) {
                next = null
            } else {
                val first = connections.removeAt(0)
                next = first.node
                if (pathNodes.contains(next)) {
                    next = null
                } else {
                    pathNodes.addNode(next)
                    if (next !== destination) {
                        connections.filter(Connection::isValid).forEach { Ant(this, it) }
                    }
                }
                cost += first.cost
            }
        }
        network.removeAnt(this, next === destination)
    }

    val nodes: List<Node> get() = pathNodes.nodes
    val networkRef: Network get() = network
}
