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

    network.connect(0, 1, 0, -1)
    network.connect(1, 2, 0, -1)
    network.connect(0, 2, 1, -1)
    network.connect(0, 3, 1, -1)
    network.connect(3, 2, 1, -1)
  }

  @Test
  fun testCost() {
    setup()
    val path = network.findPath(0, 2, Optimization.C).get()
    assertEquals(listOf(n1, n2, n3), path.getNodes())
    assertEquals(0, path.getCost())
  }

  @Test
  fun testNodes() {
    setup()
    val path = network.findPath(0, 2, Optimization.N).get()
    assertEquals(listOf(n1, n3), path.getNodes())
    assertEquals(1, path.getCost())
  }
}
