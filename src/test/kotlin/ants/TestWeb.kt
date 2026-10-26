package ants

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TestWeb {

  @LocalServerPort
  var port: Int = 0

  @Autowired
  lateinit var restTemplate: TestRestTemplate

  val paths =
    withoutWhitespace(javaClass.getResourceAsStream("/response.json")!!.readAllBytes().toString(Charsets.UTF_8))

  val network =
    withoutWhitespace(javaClass.getResourceAsStream("/network.json")!!.readAllBytes().toString(Charsets.UTF_8))

  val path =
    withoutWhitespace(javaClass.getResourceAsStream("/path.json")!!.readAllBytes().toString(Charsets.UTF_8))

  val status =
    withoutWhitespace(javaClass.getResourceAsStream("/status.json")!!.readAllBytes().toString(Charsets.UTF_8))

  val id = paths.substring(7, paths.indexOf("\"", 7))

  private fun findPaths(): ResponseEntity<String> {
    val payload = javaClass.getResourceAsStream("/request.json")!!.readAllBytes().toString(Charsets.UTF_8)
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    return restTemplate.postForEntity<String>(
      "http://localhost:$port/findPaths",
      HttpEntity(payload, headers)
    )
  }

  private fun storeNetwork(): ResponseEntity<String> {
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    return restTemplate.postForEntity<String>(
      "http://localhost:$port/storeNetwork",
      HttpEntity(network, headers)
    )
  }

  private fun getPaths(id: String) =
    restTemplate.getForObject("http://localhost:$port/getPaths/$id", String::class.java)

  private fun findPath(id: String, from: Int, to: Int) =
    restTemplate.getForObject("http://localhost:$port/findPath/$id?from=$from&to=$to", String::class.java)

  private fun deleteAll(id: String): ResponseEntity<String> =
    restTemplate.exchange<String>("http://localhost:$port/deleteAll/$id", HttpMethod.DELETE)

  private fun getNetwork(id: String) =
    restTemplate.getForObject("http://localhost:$port/getNetwork/$id", String::class.java)

  private fun withoutWhitespace(value: String) =
    value.replace("\\s".toRegex(), "")

  @Test
  @Order(1)
  fun testStoreNetwork() {
    val response =
      withoutWhitespace(javaClass.getResourceAsStream("/id.json")!!.readAllBytes().toString(Charsets.UTF_8))
    assertEquals(response, storeNetwork().body)
  }

  @Test
  @Order(2)
  fun testFindPaths() {
    val response = findPaths()
    assertEquals(HttpStatusCode.valueOf(200), response.statusCode)
    assertEquals(paths, withoutWhitespace(response.body!!))
  }

  @Test
  @Order(3)
  fun testGetPaths() {
    assertEquals(paths, getPaths(id)!!)
  }

  @Test
  @Order(4)
  fun testGetNetwork() {
    assertEquals(network, getNetwork(id)!!)
  }

  @Test
  @Order(5)
  fun testFindPath() {
    assertEquals(path, findPath(id, 0, 1)!!)
  }

  @Test
  @Order(6)
  fun testDeleteAll() {
    assertEquals(status, deleteAll(id).body)
  }
}
