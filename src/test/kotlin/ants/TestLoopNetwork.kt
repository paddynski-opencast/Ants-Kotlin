package ants

import ants.models.Network
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TestLoopNetwork {
    @Test fun testPath() {
        val network = Network()
        val l1 = network.addNode(0)
        val l2 = network.addNode(1)
        val l3 = network.addNode(2)
        val l4 = network.addNode(3)
        network.connect(0, 1); network.connect(1, 2); network.connect(2, 0); network.connect(1, 3, 1, 1)
        assertEquals(listOf(l1, l2, l4), network.findPath(0, 3).get().getNodes())
    }
}
