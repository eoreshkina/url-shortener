package dkb.url_shortner

import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.Base64

@Service
class ShortenerService(
    private val repository: ShortenerRepository
) {
    fun shortenUrl(originalUrl: String): UrlMapping {

        // Check if shorten url already exists
        repository.findByLongUrl(originalUrl)?.let { return it }

        // Generate new shorten url
        val shortUrl = generateShortUrl(originalUrl)

        // Save new mapping
        return repository.save(UrlMapping(longUrl = originalUrl, shortUrl = shortUrl))
    }

    private fun generateShortUrl(originalUrl: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(originalUrl.toByteArray())
        val encoded = Base64.getUrlEncoder().encodeToString(digest).substring(0, 6)
        return encoded
    }

    fun getOriginalUrl(shortUrl: String) : String? {
        val originalUrl = repository.findByShortUrl(shortUrl)?.longUrl
        return originalUrl ?: throw UrlNotFoundException("Invalid short url")
    }
}