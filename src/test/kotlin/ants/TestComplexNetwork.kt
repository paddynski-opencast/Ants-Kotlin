package ants

import ants.models.Network
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TestComplexNetwork {
    private lateinit var network: Network
    private lateinit var l1: ants.models.Node
    private lateinit var l2: ants.models.Node
    private lateinit var l3: ants.models.Node
    private lateinit var l4: ants.models.Node
    private lateinit var l5: ants.models.Node

    private fun setup() {
        network = Network()
        l1 = network.addNode(0); l2 = network.addNode(1); l3 = network.addNode(2); l4 = network.addNode(3); l5 = network.addNode(4)
        network.connect(0, 1, 1, -1); network.connect(1, 2)
        network.connect(0, 3, 2, 2); network.connect(3, 2)
        network.connect(0, 4, -1, 1); network.connect(4, 2)
    }

    @Test fun testPath0to3() {
        setup()
        val path = network.findPath(0, 2).get()
        assertEquals(listOf(l1, l2, l3), path.getNodes()); assertEquals(1, path.getCost())
    }
    @Test fun testPath2to0() {
        setup()
        val path = network.findPath(2, 0).get()
        assertEquals(listOf(l3, l5, l1), path.getNodes()); assertEquals(1, path.getCost())
    }
    @Test fun testPath2to0with4Closed() {
        setup(); network.closeNode(4)
        val path = network.findPath(2, 0).get()
        assertEquals(listOf(l3, l4, l1), path.getNodes()); assertEquals(2, path.getCost())
    }
}
