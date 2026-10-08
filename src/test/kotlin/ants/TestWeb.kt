package ants

import ants.controllers.NetworkController
import ants.external.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TestWeb {
    @LocalServerPort
    var port: Int = 0

    @Autowired lateinit var restTemplate: TestRestTemplate

    private fun post(isJson: Boolean, useAuth: Boolean): ResponseEntity<String> {
        val payload = javaClass.getResourceAsStream("/request.json")!!.readAllBytes().toString(Charsets.UTF_8)
        val headers = HttpHeaders()
        if (isJson) headers.contentType = MediaType.APPLICATION_JSON
        if (useAuth) headers.setBearerAuth(Token.getToken(payload.toByteArray().size))
        return restTemplate.postForEntity(
            "http://localhost:$port${NetworkController.FINDPATHS}",
            HttpEntity(payload, headers),
            String::class.java
        )
    }

    private fun get(id: String) =
        restTemplate.getForObject("http://localhost:$port${NetworkController.GETPATHS}?id=$id", String::class.java)

    private fun withoutWhitespace(value: String) = value.replace("\\s".toRegex(), "")

    @Test fun testOk() {
        val expected = javaClass.getResourceAsStream("/response.json")!!.readAllBytes().toString(Charsets.UTF_8)
        val response = post(true, true)
        assertEquals(HttpStatusCode.valueOf(200), response.statusCode)
        assertEquals(withoutWhitespace(expected), withoutWhitespace(response.body!!))
    }

    @Test fun testUnauthorized() {
        assertEquals(HttpStatusCode.valueOf(401), post(true, false).statusCode)
    }

    @Test fun testNotJson() {
        assertEquals(HttpStatusCode.valueOf(415), post(false, true).statusCode)
    }

    @Test fun testGet() {
        val expected = withoutWhitespace(javaClass.getResourceAsStream("/response.json")!!.readAllBytes().toString(Charsets.UTF_8))
        val id = expected.substring(7, expected.indexOf("\"", 7))
        restTemplate.delete("http://localhost:$port/delete?id=$id")
        post(true, true)
        assertEquals(expected, get(id)!!)
    }
}
