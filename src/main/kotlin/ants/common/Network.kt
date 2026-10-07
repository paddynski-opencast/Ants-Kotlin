package ants.common
interface Network {
    fun getMode(id: Int): Node
    fun addNode(id: Int): Node
    fun addNode(id: Int, closed: Boolean): Node
    fun closeNode(id: Int)
    fun openMode(id: Int)
    fun connect(from: Int, to: Int)
    fun connect(from: Int, to: Int, cost: Int)
    fun connect(from: Int, to: Int, costA: Int, costB: Int)
}
