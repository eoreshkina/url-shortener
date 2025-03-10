package dkb.url_shortener

import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.Base64

@Service
class ShortenerService(
    private val repository: ShortenerRepository
) {
    fun shortenUrl(longUrl: String): UrlMapping {

        // Check if shorten url already exists
        repository.findByLongUrl(longUrl)?.let { return it }

        // Generate new shorten url
        val shortUrl = generateShortCode(longUrl)

        // Save new mapping
        return repository.save(UrlMapping(longUrl = longUrl, shortUrl = shortUrl))
    }

    private fun generateShortCode(longUrl: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(longUrl.toByteArray())
        val encoded = Base64.getUrlEncoder().encodeToString(digest).substring(0, 6)
        return encoded
    }

    fun getLongUrl(shortUrl: String) : String? {
        val longUrl = repository.findByShortUrl(shortUrl)?.longUrl
        return longUrl ?: throw UrlNotFoundException("Invalid short url")
    }
}