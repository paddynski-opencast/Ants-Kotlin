package ants

import ants.models.Network
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestLoopNetwork {

  @Test
  fun testPath() {
    val network = Network()
    val n1 = network.addNode(0)
    val n2 = network.addNode(1)
    val n3 = network.addNode(2)
    val n4 = network.addNode(3)

    network.connect(0, 1)
    network.connect(1, 2)
    network.connect(2, 0)
    network.connect(1, 3, 1, 1)

    assertEquals(listOf(n1, n2, n4), network.findPath(0, 3).get().getNodes())
  }
}
