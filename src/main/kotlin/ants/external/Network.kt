package ants.external

import ants.common.Direction
import ants.common.Errors
import ants.common.NetworkException
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.nio.charset.StandardCharsets
import java.util.Base64

open class Network : IRequest, ants.common.Network {
    @JsonProperty var name: String
    @JsonProperty var nodes: MutableList<Node>
    @JsonProperty var connections: MutableList<Connection>

    constructor() : this("")

    constructor(name: String) {
        this.name = name
        nodes = mutableListOf()
        connections = mutableListOf()
    }

    constructor(network: ants.models.Network) {
        name = network.getName()
        nodes = network.getNodeData().toMutableList()
        connections = network.getConnectionData().toMutableList()
    }

    constructor(data: Network) {
        this.name = data.name
        this.nodes = data.nodes
        this.connections = data.connections
    }

    private fun invalidCosts(costs: Collection<Int>) {
        if (costs.any { it < ants.models.Network.INVALID }) Errors.error(NetworkException.ERROR_CONNECT, "Invalid cost(s)!")
    }

    protected fun nodeExists(id: Int): Boolean {
        if (id < 0) {
            Errors.error(NetworkException.ERROR_INVALID, "Invalid 'id'!")
            return true
        }
        return nodes.any { it.id == id }
    }

    private fun getN(id: Int) = nodes.firstOrNull { it.id == id }

    private fun setNodeState(id: Int, state: Boolean) {
        val node = getN(id)
        if (node == null) {
            Errors.doesNotExist(Node::class.java, id)
            return
        }
        node.closed = state
    }

    override fun closeNode(id: Int) = setNodeState(id, true)

    override fun openMode(id: Int) = setNodeState(id, false)

    override fun getMode(id: Int): ants.common.Node =
        getN(id) ?: run {
            Errors.doesNotExist(Node::class.java, id)
            throw AssertionError()
        }

    override fun addNode(id: Int) = addNode(id, false)

    override fun addNode(id: Int, closed: Boolean): ants.common.Node {
        if (!nodeExists(id)) return Node(id, closed).also(nodes::add)
        Errors.alreadyExists(Node::class.java, id)
        throw AssertionError()
    }

    override fun connect(from: Int, to: Int) = connect(from, to, mapOf(Direction.A to 0, Direction.B to 0))

    override fun connect(from: Int, to: Int, cost: Int) = connect(from, to, mapOf(Direction.A to cost))

    override fun connect(from: Int, to: Int, costA: Int, costB: Int) =
        connect(from, to, mapOf(Direction.A to costA, Direction.B to costB))

    private fun connect(from: Int, to: Int, costs: Map<Direction, Int>) {
        if (to != from && nodeExists(from) && nodeExists(to)) {
            invalidCosts(costs.values)
            costs[Direction.A]?.takeIf { it > ants.models.Network.INVALID }
                ?.let { connections.add(Connection(from, to, Direction.A, it)) }
            costs[Direction.B]?.takeIf { it > ants.models.Network.INVALID }
                ?.let { connections.add(Connection(to, from, Direction.B, it)) }
            return
        }
        Errors.cannotCreate(Connection::class.java)
    }

    override fun equals(other: Any?) = other is Network && hashCode() == other.hashCode()

    fun id(): String =
        Base64.getEncoder().encodeToString(hashCode().toString().toByteArray(StandardCharsets.UTF_8))

    override fun hashCode(): Int {
        val hashes = mutableListOf(name)
        hashes += nodes.map(Node::hash).sorted()
        hashes += connections.map(Connection::hash).sorted()
        return hashes.hashCode()
    }

    constructor(src: NetworkWithSearch) {
        name = src.name
        nodes = src.nodes
        connections = src.connections
    }
}
