package ants

import ants.models.Network
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class TestSimpleNetwork {
    @Test fun testPath() {
        val network = Network()
        val l1 = network.addNode(0)
        val l2 = network.addNode(1)
        network.connect(0, 1)
        assertEquals(listOf(l1), network.findPath(0, 0).get().getNodes())
        assertEquals(listOf(l1, l2), network.findPath(0, 1).get().getNodes())
    }

    @Test fun testNoPath() {
        val network = Network()
        network.addNode(0); network.addNode(1)
        assertTrue(network.findPath(0, 1).isEmpty)
    }

    @Test fun testOpenCloseNode() {
        val network = Network()
        val l1 = network.addNode(0)
        val l2 = network.addNode(1, true)
        val l3 = network.addNode(2)
        network.connect(0, 1); network.connect(1, 2)
        assertTrue(network.findPath(0, 2).isEmpty)
        network.openMode(1)
        assertEquals(listOf(l1, l2, l3), network.findPath(0, 2).get().getNodes())
    }
}
