package dkb.url_shortener

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class UrlMapping(
    @Id
    val id: UUID = UUID.randomUUID(),
    val longUrl: String,
    val shortUrl: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
)
