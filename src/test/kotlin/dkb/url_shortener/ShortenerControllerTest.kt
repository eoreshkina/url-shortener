package dkb.url_shortener

import com.fasterxml.jackson.databind.ObjectMapper
import dkb.url_shortener.dto.UrlCreationRequest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content


@WebMvcTest(ShortenerController::class)
class ShortenerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var service: ShortenerService

    private val testUrl = "http://localhost:8080/test"
    private val testShortUrl = "test"

    @Test
    fun `should create short url`() {
        val request = UrlCreationRequest(longUrl = testUrl)
        val urlMapping = UrlMapping(longUrl = testUrl, shortUrl = testShortUrl)

        `when`(service.shortenUrl(testUrl)).thenReturn(urlMapping)

        mockMvc.perform(
            post("/api/url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.shortUrl").isNotEmpty)
            .andExpect(jsonPath("$.createdAt").isNotEmpty)
    }

    @Test
    fun `should return to original url`() {
        `when`(service.getLongUrl(testShortUrl)).thenReturn(testUrl)

        mockMvc.perform(
            get("/$testShortUrl")
        ).andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl(testUrl))
    }

    @Test
    fun `should return 404 when short url does not exist`() {
        `when`(service.getLongUrl(testShortUrl)).thenReturn(null)

        mockMvc.perform(
            get("/$testShortUrl")
        ).andExpect(status().isNotFound)
            .andExpect(content().string("Short URL is not found"))
    }
}