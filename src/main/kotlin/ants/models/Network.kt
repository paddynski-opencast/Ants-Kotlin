package ants.models

import ants.common.Direction
import ants.common.Errors
import ants.common.NetworkException
import ants.common.Optimization
import java.util.*

class Network : ants.common.Network {
  private val name: String
  private var ants = 0
  private val nodes = mutableListOf<Node>()
  private val paths = mutableListOf<Path>()

  constructor(name: String) {
    this.name = name
  }

  constructor() : this("")

  private constructor(network: Network) {
    name = network.name
    ants = network.ants
    nodes.addAll(network.nodes)
    paths.addAll(network.paths)
  }

  constructor(data: ants.external.Network) {
    name = data.name
    data.nodes.forEach { addNode(it.id, it.closed) }
    data.connections.forEach {
      connectNodes(
        getNode(it.from),
        getNode(it.to),
        it.direction,
        it.cost
      )
    }
  }

  private fun invalidCosts(costs: Collection<Int>) {
    if (costs.any { it < INVALID }) Errors.error(NetworkException.ERROR_CONNECT, "Invalid cost(s)!")
  }

  private fun getN(id: Int) = nodes.singleOrNull { it.id == id }

  override fun getNode(id: Int): Node =
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

  override fun connect(from: Int, to: Int) =
    connect(from, to, mapOf(Direction.A to 0, Direction.B to 0))

  override fun connect(from: Int, to: Int, cost: Int) =
    connect(from, to, mapOf(Direction.A to cost))

  override fun connect(from: Int, to: Int, costA: Int, costB: Int) =
    connect(from, to, mapOf(Direction.A to costA, Direction.B to costB))

  private fun connect(from: Int, to: Int, costs: Map<Direction, Int>) {
    if (to != from) {
      val fromNode = getNode(from)
      val toNode = getNode(to)
      invalidCosts(costs.values)
      costs[Direction.A]?.takeIf { it > INVALID }?.let {
        connectNodes(fromNode, toNode, Direction.A, it)
      }
      costs[Direction.B]?.takeIf { it > INVALID }?.let {
        connectNodes(toNode, fromNode, Direction.B, it)
      }
      return
    }
    Errors.error(NetworkException.ERROR_CONNECT, "Cannot connect connect node to itself! - [$from]", from)
  }

  private fun connectNodes(from: Node, to: Node, direction: Direction, cost: Int) {
    if (!from.connect(direction, to, cost)) {
      Errors.error(NetworkException.ERROR_CONNECT, "Nodes already connected! $from - $to")
    }
  }

  fun findPath(from: Int, to: Int, optimization: Optimization = Optimization.CS): Optional<Path> {
    val fromNode = getNode(from)
    val toNode = getNode(to)
    if (fromNode.isClosed() || toNode.isClosed()) return Optional.empty()
    if (fromNode === toNode) return Optional.of(Path(fromNode))
    if (ants != 0) return Network(this).findPath(from, to, optimization)
    paths.clear()
    Ant(this, fromNode, toNode, Direction.A)
    Ant(this, fromNode, toNode, Direction.B)
    while (ants > 0) Thread.yield()
    val returnPaths = when(optimization) {
      Optimization.CS -> optimizeOnShortest(optimizeOnMinCost(paths))
      Optimization.CL -> optimizeOnLongest(optimizeOnMinCost(paths))
      Optimization.SC -> optimizeOnMinCost(optimizeOnShortest(paths))
      Optimization.LC -> optimizeOnMinCost(optimizeOnLongest(paths))
      Optimization.ES -> optimizeOnShortest(optimizeOnMaxCost(paths))
      Optimization.EL -> optimizeOnLongest(optimizeOnMaxCost(paths))
      Optimization.SE -> optimizeOnMaxCost(optimizeOnShortest(paths))
      Optimization.LE -> optimizeOnMaxCost(optimizeOnLongest(paths))
    }
    return Optional.ofNullable(returnPaths.getOrNull(0))
  }

  private fun optimizeOnMinCost(paths: List<Path>): List<Path> {
    val cost = paths.minOfOrNull { it.getCost() } ?: INVALID
    return paths.filter { it.getCost() == cost }
  }

  private fun optimizeOnMaxCost(paths: List<Path>): List<Path> {
    val cost = paths.maxOfOrNull { it.getCost() } ?: INVALID
    return paths.filter { it.getCost() == cost }
  }

  private fun optimizeOnShortest(paths: List<Path>): List<Path> {
    val nodes = paths.minOfOrNull { it.getNodes().size } ?: INVALID
    return paths.filter { it.getNodes().size == nodes }
  }

  private fun optimizeOnLongest(paths: List<Path>): List<Path> {
    val nodes = paths.maxOfOrNull { it.getNodes().size } ?: INVALID
    return paths.filter { it.getNodes().size == nodes }
  }

  override fun toString() = name

  fun getNodeData() = nodes.map(Node::getData)

  fun getConnectionData() = nodes.asSequence().flatMap(Node::getConnectionData).toList()

  @Synchronized
  internal fun addAnt() {
    ants++
  }

  @Synchronized
  internal fun removeAnt(ant: Ant, hasReachedDestination: Boolean) {
    if (hasReachedDestination) paths.add(Path(ant))
    ants--
  }

  fun getName() = name
}
