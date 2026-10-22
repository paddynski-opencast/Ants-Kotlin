package ants

import ants.common.Optimization
import ants.models.Network
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestOptimization {
  private lateinit var network: Network
  private lateinit var n1: ants.models.Node
  private lateinit var n2: ants.models.Node
  private lateinit var n3: ants.models.Node

  private fun setup() {
    network = Network()
    n1 = network.addNode(0)
    n2 = network.addNode(1)
    n3 = network.addNode(2)
    network.addNode(3)
    network.addNode(4)
    network.addNode(5)
    network.addNode(6)
    network.addNode(7)
    network.addNode(8)
    network.addNode(9)
    network.addNode(10)

    network.connect(0, 1, 0, -1)
    network.connect(1, 2, 0, -1)
    network.connect(0, 3, 1, -1)
    network.connect(3, 2, 1, -1)
    network.connect(0, 4, 1, -1)
    network.connect(4, 5, 1, -1)
    network.connect(5, 2, 1, -1)
    network.connect(0, 6, 1, -1)
    network.connect(6, 2, 1, -1)
    network.connect(0, 7, 1, -1)
    network.connect(7, 8, 1, -1)
    network.connect(8, 9, 1, -1)
    network.connect(9, 10, 1, -1)
    network.connect(10, 0, 1, -1)
  }

  @Test
  fun testCost() {
    setup()

    // There are two paths with a cost of 0
    // -  [0], [1], [2]
    // -  [0], [4], [5], [2]
    // The system will pick the path with the minimum number of notes
    // from the above
    val path = network.findPath(0, 2, Optimization.C).get()
    assertEquals(listOf(n1, n2, n3), path.getNodes())
    assertEquals(0, path.getCost())
  }

  @Test
  fun testNodes() {
    setup()

    // There are two paths with 3 nodes
    // -  [0], [1], [2] => 0
    // -  [0], [6], [2] => 2
    // The system will pick the path with the minimum cost
    // from the above
    val path = network.findPath(0, 2, Optimization.N).get()
    assertEquals(listOf(n1, n2, n3), path.getNodes())
    assertEquals(0, path.getCost())
  }
}
