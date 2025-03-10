package dkb.url_shortener

import dkb.url_shortener.dto.UrlCreationRequest
import dkb.url_shortener.dto.UrlCreationResponse
import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.view.RedirectView
import java.time.format.DateTimeFormatter

@Controller
@Slf4j
class ShortenerController(private val shortenerService: ShortenerService) {

    val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @PostMapping("/api/url")
    fun createShortUrl(
        @RequestBody request: UrlCreationRequest,
        servletRequest: HttpServletRequest
    ): ResponseEntity<UrlCreationResponse> {
        val urlMapping = shortenerService.shortenUrl(request.longUrl)

        // Build the complete short URL including host
        val baseUrl = servletRequest.requestURL.toString().replace(servletRequest.requestURI, "")
        val shortUrl = "$baseUrl/${urlMapping.shortUrl}"

        return ResponseEntity.ok(
            UrlCreationResponse(
                id = urlMapping.id,
                shortUrl = shortUrl,
                createdAt = urlMapping.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        )
    }

    @GetMapping("{shortCode}")
    fun returnToOriginalUrl(@PathVariable shortCode: String): Any {
        val longUrl = shortenerService.getLongUrl(shortCode)

        log.info("Original URL for short code $shortCode is $longUrl")

        return if (longUrl != null) {
            RedirectView(longUrl)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Short URL is not found")
        }
    }
}