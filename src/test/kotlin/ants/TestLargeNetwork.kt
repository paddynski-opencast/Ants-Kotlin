package ants

import ants.common.NetworkException
import ants.models.Network
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class TestLargeNetwork {
  private val nodes = 65536
  private val levels = 8

  private class Pyramid {
    private val data = mutableListOf<MutableList<MutableList<Int>>>()

    var from: Int = -1
    var to: Int = -1
    val expected = mutableListOf<Int>()

    constructor(levels: Int) {
      var id = 0
      for (l in 1..levels) {
        val row = mutableListOf<MutableList<Int>>()
        for (r in 1..l) {
          val col = mutableListOf<Int>()
          for (c in 1..l) {
            col.add(id)
            if (r == l && c == l) expected.add(id)
            id++
          }
          row.add(col)
        }
        data.add(row)
      }
      val l = expected.size - 1
      val f = Random.nextInt(l - 1)
      val t = Random.nextInt(f, l)
      from = expected[f]
      to = expected[t]
      expected.removeIf { it !in from..to }
    }

    private fun getId(l: Int, r: Int, c: Int): Int? {
      val row = data.getOrNull(l) ?: return null
      val col = row.getOrNull(r) ?: return null
      return col.getOrNull(c)
    }

    private fun getParents(l: Int, r: Int, c: Int): List<Int> =
      listOfNotNull(
        getId(l - 1, r - 1, c - 1),
        getId(l - 1, r - 1, c),
        getId(l - 1, r, c - 1),
        getId(l - 1, r, c)
      )

    fun getNetwork(): Network {
      val network = Network()
      data.forEachIndexed { l, row ->
        row.forEachIndexed { r, col ->
          col.forEachIndexed { c, id ->
            network.addNode(id)
            getParents(l, r, c).forEach {
              network.connect(it, id, 0, -1)
            }
          }
        }
      }
      return network
    }
  }

  @Test
  fun testLargeNetwork() {
    val from = Random.nextInt(nodes - 1)
    val to = Random.nextInt(from + 1, nodes)
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

  @Test
  fun testPyramid() {
    val pyramid = Pyramid(levels)
    val start = System.currentTimeMillis()
    val path = pyramid.getNetwork().findPath(pyramid.from, pyramid.to)
    val time = System.currentTimeMillis() - start
    assertEquals(pyramid.expected, path.get().getNodes().map { it.id })
    assert(time < 2000)
  }

//  @Test
//  fun testOutOfMemory() {
//    assertThrows<NetworkException> {
//      val pyramid = Pyramid(3 * levels)
//      pyramid.getNetwork().findPath(pyramid.from, pyramid.to)
//    }
//  }
}
