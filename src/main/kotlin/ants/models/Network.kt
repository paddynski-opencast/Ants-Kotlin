package ants.models

import ants.common.Direction
import ants.common.Errors
import ants.common.NetworkException
import ants.external.Connection
import java.util.Optional

class Network : ants.common.Network {
    companion object { const val INVALID = -1 }

    private val name: String
    private var ants = 0
    private val nodes = mutableListOf<Node>()
    private val paths = mutableListOf<Path>()

    constructor(name: String) { this.name = name }
    constructor() : this("")

    private constructor(network: Network) {
        name = network.name
        ants = network.ants
        nodes.addAll(network.nodes)
        paths.addAll(network.paths)
    }

    private fun invalidCosts(costs: Collection<Int>) {
        if (costs.any { it < INVALID }) Errors.error(NetworkException.ERROR_CONNECT, "Invalid cost(s)!")
    }

    private fun getN(id: Int) = nodes.singleOrNull { it.id == id }

    override fun getMode(id: Int): Node =
        getN(id) ?: run {
            Errors.doesNotExist(Node::class.java, id)
            throw AssertionError()
        }

    override fun addNode(id: Int): Node = addNode(id, false)

    override fun addNode(id: Int, closed: Boolean): Node {
        if (id < 0 || getN(id) != null) {
            Errors.alreadyExists(Node::class.java, id)
            throw AssertionError()
        }
        return Node(id, closed).also(nodes::add)
    }

    private fun setNodeState(id: Int, state: Boolean) {
        getN(id)?.let {
            it.setClosed(state)
            return
        }
        Errors.doesNotExist(Node::class.java, id)
    }

    override fun closeNode(id: Int) = setNodeState(id, true)
    override fun openMode(id: Int) = setNodeState(id, false)

    override fun connect(from: Int, to: Int) = connect(from, to, mapOf(Direction.A to 0, Direction.B to 0))
    override fun connect(from: Int, to: Int, cost: Int) = connect(from, to, mapOf(Direction.A to cost))
    override fun connect(from: Int, to: Int, costA: Int, costB: Int) =
        connect(from, to, mapOf(Direction.A to costA, Direction.B to costB))

    private fun connect(from: Int, to: Int, costs: Map<Direction, Int>) {
        if (to != from) {
            val fromNode = getMode(from)
            val toNode = getMode(to)
            invalidCosts(costs.values)
            costs[Direction.A]?.takeIf { it > INVALID }?.let { fromNode.connect(Direction.A, toNode, it) }
            costs[Direction.B]?.takeIf { it > INVALID }?.let { toNode.connect(Direction.B, fromNode, it) }
            return
        }
        Errors.error(NetworkException.ERROR_CONNECT, "Connect connect node to itself! - [$from]", from)
    }

    fun findPath(from: Int, to: Int): Optional<Path> {
        val fromNode = getMode(from)
        val toNode = getMode(to)
        if (fromNode === toNode) return Optional.of(Path(fromNode))
        if (ants != 0) return Network(this).findPath(from, to)
        paths.clear()
        Ant(this, fromNode, toNode, Direction.A)
        Ant(this, fromNode, toNode, Direction.B)
        while (ants > 0) Thread.yield()
        val cost = paths.minOfOrNull(Path::getCost) ?: 0
        return Optional.ofNullable(paths.firstOrNull { it.getCost() == cost })
    }

    override fun toString() = name

    fun getNodeData() = nodes.map(Node::getData)
    fun getConnectionData() = nodes.asSequence().flatMap(Node::getConnectionData).toList()

    @Synchronized internal fun addAnt() { ants++ }

    @Synchronized internal fun removeAnt(ant: Ant, hasReachedDestination: Boolean) {
        if (hasReachedDestination) paths.add(Path(ant))
        ants--
    }

    fun getName() = name
}
