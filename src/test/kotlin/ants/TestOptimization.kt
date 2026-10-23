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
  private lateinit var n4: ants.models.Node
  private lateinit var n5: ants.models.Node
  private lateinit var n6: ants.models.Node
  private lateinit var n7: ants.models.Node
  private lateinit var n8: ants.models.Node
  private lateinit var n9: ants.models.Node

  private fun setup() {
    network = Network()
    n1 = network.addNode(0)
    n2 = network.addNode(1)
    n3 = network.addNode(2)
    network.addNode(3)
    n8 = network.addNode(4)
    n9 = network.addNode(5)
    network.addNode(6)
    n4 = network.addNode(7)
    n5 = network.addNode(8)
    n6 = network.addNode(9)
    n7 = network.addNode(10)

    network.connect(0, 1, 0, -1)
    network.connect(1, 2, 0, -1)

    network.connect(0, 3, 1, -1)
    network.connect(3, 2, 1, -1)

    network.connect(0, 4, 0, -1)
    network.connect(4, 5, 0, -1)
    network.connect(5, 2, 0, -1)

    network.connect(0, 6, 1, -1)
    network.connect(6, 2, 1, -1)

    network.connect(0, 7, 1, -1)
    network.connect(7, 8, 1, -1)
    network.connect(8, 9, 1, -1)
    network.connect(9, 10, 1, -1)
    network.connect(10, 2, 1, -1)
  }

  @Test
  fun testCostShortest() {
    setup()

    // There are two paths with a cost of 0
    // -  [0], [1], [2]
    // -  [0], [4], [5], [2]
    // The system will pick the path with the minimum number of notes
    // from the above
    val path = network.findPath(0, 2, Optimization.CS).get()
    assertEquals(listOf(n1, n2, n3), path.getNodes())
    assertEquals(0, path.getCost())
  }

  @Test
  fun testCostLongest() {
    setup()

    // There are two paths with a cost of 0
    // -  [0], [1], [2]
    // -  [0], [4], [5], [2]
    // The system will pick the path with the maximum number of notes
    // from the above
    val path = network.findPath(0, 2, Optimization.CL).get()
    assertEquals(listOf(n1, n8, n9, n3), path.getNodes())
    assertEquals(0, path.getCost())
  }

  @Test
  fun testShortest() {
    setup()

    // There are two paths with 3 nodes
    // -  [0], [1], [2] => 0
    // -  [0], [6], [2] => 2
    // The system will pick the path with the minimum cost
    // from the above
    val path = network.findPath(0, 2, Optimization.SC).get()
    assertEquals(listOf(n1, n2, n3), path.getNodes())
    assertEquals(0, path.getCost())
  }

  @Test
  fun testLongest() {
    setup()

    val path = network.findPath(0, 2, Optimization.LC).get()
    assertEquals(listOf(n1, n4, n5, n6, n7, n3), path.getNodes())
    assertEquals(5, path.getCost())
  }
}
