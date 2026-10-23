package ants

import ants.common.Optimization
import ants.models.Network
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.exp

class TestOptimization {
  private fun testOptimization(optimization: Optimization, expectedIds: List<Int>, expectedCost: Int) {
    val network = Network()
    network.addNode(0)
    network.addNode(1)
    network.addNode(2)
    network.addNode(4)
    network.addNode(5)
    network.addNode(6)
    network.addNode(7)
    network.addNode(8)
    network.addNode(9)
    network.addNode(10)
    network.addNode(11)
    network.addNode(12)
    network.addNode(13)
    network.addNode(14)

    network.connect(0, 1, 0, -1)
    network.connect(1, 2, 0, -1)

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

    network.connect(0, 9, 3, -1)

    network.connect(0, 11, 0, -1)
    network.connect(11, 12, 0, -1)
    network.connect(12, 13, 0, -1)
    network.connect(13, 14, 2, -1)
    network.connect(14, 2, 2, -1)

    val path = network.findPath(0, 2, optimization).get()
    assertEquals(expectedIds, path.getNodes().map { it.id })
    assertEquals(expectedCost, path.getCost())
  }

  @Test
  fun testMinCostShortest() {

    // There are two paths with a cost of 0
    // -  [0], [1], [2]
    // -  [0], [4], [5], [2]
    // The system will pick the path with the minimum number of notes
    // from the above
    testOptimization(
      optimization = Optimization.CS,
      expectedIds = listOf(0, 1, 2),
      expectedCost = 0
    )
  }

  @Test
  fun testMinCostLongest() {

    // There are two paths with a cost of 0
    // -  [0], [1], [2]
    // -  [0], [4], [5], [2]
    // The system will pick the path with the maximum number of notes
    // from the above
    testOptimization(
      optimization = Optimization.CL,
      expectedIds = listOf(0, 4, 5, 2),
      expectedCost = 0
    )
  }

  @Test
  fun testShortestMinCost() {

    // There are two paths with 3 nodes
    // -  [0], [1], [2] => 0
    // -  [0], [6], [2] => 2
    // The system will pick the path with the minimum cost
    // from the above
    testOptimization(
      optimization = Optimization.SC,
      expectedIds = listOf(0, 1, 2),
      expectedCost = 0
    )
  }

  @Test
  fun testLongestMinCost() {

    // There are two paths with 6 nodes
    // -  [0], [7], [8], [9], [10], [2] => 5
    // -  [0], [11], [12], [13], [14], [2] => 4
    // The system will pick the path with the minimum cost
    // from the above
    testOptimization(
      optimization = Optimization.LC,
      expectedIds = listOf(0, 11, 12, 13, 14, 2),
      expectedCost = 4
    )
  }

  @Test
  fun testMaxCostShortest() {

    // There are two paths with a cost of 5
    // -  [0], [9], [10], [2]
    // -  [0], [7], [8], [9], [10], [2]
    // The system will pick the path with the minimum number of notes
    // from the above
    testOptimization(
      optimization = Optimization.ES,
      expectedIds = listOf(0, 9, 10, 2),
      expectedCost = 5
    )
  }

  @Test
  fun testMaxCostLongest() {

    // There are two paths with a cost of 5
    // -  [0], [9], [10], [2]
    // -  [0], [7], [8], [9], [10], [2]
    // The system will pick the path with the maximum number of notes
    // from the above
    testOptimization(
      optimization = Optimization.EL,
      expectedIds = listOf(0, 7, 8, 9, 10, 2),
      expectedCost = 5
    )
  }

  @Test
  fun testShortestMaxCost() {

    // There are two paths with 3 nodes
    // -  [0], [1], [2] => 0
    // -  [0], [6], [2] => 2
    // The system will pick the path with the minimum cost
    // from the above
    testOptimization(
      optimization = Optimization.SE,
      expectedIds = listOf(0, 6, 2),
      expectedCost = 2
    )
  }

  @Test
  fun testLongestMaxCost() {

    // There are two paths with 6 nodes
    // -  [0], [7], [8], [9], [10], [2] => 5
    // -  [0], [11], [12], [13], [14], [2] => 4
    // The system will pick the path with the maximum cost
    // from the above
    testOptimization(
      optimization = Optimization.LE,
      expectedIds = listOf(0, 7, 8, 9, 10, 2),
      expectedCost = 5
    )
  }
}
