package ants

import ants.common.NetworkException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestExceptionsForModel {
  private fun network() = ants.models.Network()

  @Test
  fun testNoInvalidNodes() {
    assertThrows<NetworkException> {
      network().getNode(0)
    }
  }

  @Test
  fun testNoDuplicateNodes() {
    assertThrows<NetworkException> { network().apply {
      addNode(0)
      addNode(0)
    }}
  }

  @Test
  fun testNoConnectNodes() {
    assertThrows<NetworkException> {
      network().connect(0, 1)
    }
  }

  @Test
  fun testNoOpenNode() {
    assertThrows<NetworkException> {
      network().openMode(0)
    }
  }

  @Test
  fun testNoCloseNode() {
    assertThrows<NetworkException> {
      network().closeNode(0)
    }
  }

  @Test
  fun testNoConnectToSelf() {
    assertThrows<NetworkException> { network().apply {
      addNode(0)
      connect(0, 0)
    }}
  }

  @Test
  fun testNoInvalidCost() {
    assertThrows<NetworkException> { network().apply {
      addNode(0)
      addNode(1)
      connect(0, 1, -2)
    }}
  }

  @Test
  fun testNoDuplicateConnections() {
    assertThrows<NetworkException> { network().apply {
      addNode(0)
      addNode(1)
      connect(0, 1)
      connect(0, 1)
    }}
    assertThrows<NetworkException> { network().apply {
      addNode(0)
      addNode(1)

      // We are trying to connect 0 to 1 in direction B
      // when 0 is already connected to 1 in direction A
      connect(0, 1, 0, -1)
      connect(1, 0, -1, 0)
    }}
    assertThrows<NetworkException> { network().apply {
      addNode(0)
      addNode(1)

      // We are trying to connect 0 to 1 in direction B
      // when 0 is already connected to 1 in direction A
      // and 1 is already connected to 0 in direction B
      connect(0, 1)
      connect(1, 0, -1, 0)
    }}
  }
}
