package ants

import ants.common.NetworkException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestExceptionsForExternal {
    private fun network() = ants.external.Network()
    @Test fun testNoInvalidNodes() { assertThrows<NetworkException> { network().getMode(0) } }
    @Test fun testNoDuplicateNodes() { assertThrows<NetworkException> { network().apply { addNode(0); addNode(0) } } }
    @Test fun testNoConnectNodes() { assertThrows<NetworkException> { network().connect(0, 1) } }
    @Test fun testNoOpenNode() { assertThrows<NetworkException> { network().openMode(0) } }
    @Test fun testNoCloseNode() { assertThrows<NetworkException> { network().closeNode(0) } }
    @Test fun testNoConnectToSelf() { assertThrows<NetworkException> { network().apply { addNode(0); connect(0, 0) } } }
    @Test fun testNoInvalidCost() { assertThrows<NetworkException> { network().apply { addNode(0); addNode(1); connect(0, 1, -2) } } }
}
