package dkb.url_shortner

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ShortenerRepository : CrudRepository<UrlMapping, UUID> {
    fun findByLongUrl(longUrl: String): UrlMapping?
    fun findByShortUrl(shortUrl: String): UrlMapping?
}