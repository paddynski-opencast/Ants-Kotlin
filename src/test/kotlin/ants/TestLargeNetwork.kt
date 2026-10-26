package ants

import ants.models.Network
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestLargeNetwork {
  private val nodes = 65536

  private val from = Random.nextInt(nodes - 1)
  private val to = Random.nextInt(from + 1, nodes)

  @Test
  fun testLargeNetwork() {
    val network = Network()
    val expected = mutableListOf(from)
    for (id in 0..<nodes) {
      network.addNode(id)
      if (id > 0) {
        network.connect(id - 1, id, 0, -1)
        if (id in (from + 1)..to) expected.add(id)
      }
    }
    val start = System.currentTimeMillis()
    val path = network.findPath(from, to)
    val time = System.currentTimeMillis() - start
    assertEquals(expected, path.get().getNodes().map { it.id })
    assert(time < 1000)
  }
}