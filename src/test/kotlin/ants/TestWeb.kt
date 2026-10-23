package ants

import ants.controllers.NetworkController
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TestWeb {

  @LocalServerPort
  var port: Int = 0

  @Autowired
  lateinit var restTemplate: TestRestTemplate

  private fun post(): ResponseEntity<String> {
    val payload = javaClass.getResourceAsStream("/request.json")!!.readAllBytes().toString(Charsets.UTF_8)
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    return restTemplate.postForEntity(
      "http://localhost:$port/findPaths",
      HttpEntity(payload, headers),
      String::class.java
    )
  }

  private fun get(id: String) =
    restTemplate.getForObject("http://localhost:$port/getPaths/$id", String::class.java)

  private fun withoutWhitespace(value: String) =
    value.replace("\\s".toRegex(), "")

  @Test
  fun testOk() {
    val expected = javaClass.getResourceAsStream("/response.json")!!.readAllBytes().toString(Charsets.UTF_8)
    val response = post()
    assertEquals(HttpStatusCode.valueOf(200), response.statusCode)
    assertEquals(withoutWhitespace(expected), withoutWhitespace(response.body!!))
  }

  @Test
  fun testGet() {
    val expected =
      withoutWhitespace(javaClass.getResourceAsStream("/response.json")!!.readAllBytes().toString(Charsets.UTF_8))
    val id = expected.substring(7, expected.indexOf("\"", 7))
    restTemplate.delete("http://localhost:$port/delete/$id")
    post()
    assertEquals(expected, get(id)!!)
  }
}
