package ants.models

import ants.common.Direction

class Path internal constructor(
    private val direction: Direction?,
    private val nodeList: List<Node>,
    private val pathCost: Int
) {
    internal constructor(ant: Ant) : this(ant.direction, ant.nodes, ant.cost)
    internal constructor(node: Node) : this(null, listOf(node), 0)

    override fun toString(): String {
        val nodeText = nodeList.joinToString(", ", "(", ")")
        return direction?.let { "[$it -> $nodeText]" } ?: nodeText
    }

    fun getStart() = nodeList.first().id
    fun getEnd() = nodeList.last().id
    fun getDirection() = direction
    fun getNodes() = nodeList
    fun getCost() = pathCost
}
