package ants.common

abstract class Network {
    companion object { const val INVALID = -1 }

    abstract fun getMode(id: Int): Node
    abstract fun addNode(id: Int): Node
    abstract fun addNode(id: Int, closed: Boolean): Node
    abstract fun closeNode(id: Int)
    abstract fun openMode(id: Int)
    abstract fun connect(from: Int, to: Int)
    abstract fun connect(from: Int, to: Int, cost: Int)
    abstract fun connect(from: Int, to: Int, costA: Int, costB: Int)
}
