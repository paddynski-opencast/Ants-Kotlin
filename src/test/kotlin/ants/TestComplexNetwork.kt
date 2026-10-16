package ants

import ants.common.Direction
import ants.models.Network
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestComplexNetwork {
  private lateinit var network: Network
  private lateinit var n1: ants.models.Node
  private lateinit var n2: ants.models.Node
  private lateinit var n3: ants.models.Node
  private lateinit var n4: ants.models.Node
  private lateinit var n5: ants.models.Node

  private fun setup() {
    network = Network()
    n1 = network.addNode(0)
    n2 = network.addNode(1)
    n3 = network.addNode(2)
    n4 = network.addNode(3)
    n5 = network.addNode(4)

    network.connect(0, 1, 1, -1); network.connect(1, 2)
    network.connect(0, 3, 2, 2); network.connect(3, 2)
    network.connect(0, 4, -1, 1); network.connect(4, 2)
  }

  @Test
  fun testPath0to3() {
    setup()
    val path = network.findPath(0, 2).get()
    assertEquals(listOf(n1, n2, n3), path.getNodes()); assertEquals(1, path.getCost())
  }

  @Test
  fun testPath2to0() {
    setup()
    val path = network.findPath(2, 0).get()
    assertEquals(listOf(n3, n5, n1), path.getNodes()); assertEquals(1, path.getCost())
  }

  @Test
  fun testPath2to0with4Closed() {
    setup(); network.closeNode(4)
    val path = network.findPath(2, 0).get()
    assertEquals(listOf(n3, n4, n1), path.getNodes()); assertEquals(2, path.getCost())
  }

  @Test
  fun testConnectedNetwork() {
    val network = Network("Test")

    val n1 = network.addNode(0)
    val n2 = network.addNode(1)
    val n3 = network.addNode(2)

    network.connect(0, 1)
    network.connect(2, 0)

    val path = network.findPath(1, 2)
    assertEquals(listOf(n2, n1, n3), path.get().getNodes())
    assertEquals(Direction.B, path.get().getDirection())
  }
}
