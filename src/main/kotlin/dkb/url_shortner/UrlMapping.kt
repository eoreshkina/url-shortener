package dkb.url_shortner

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class UrlMapping(
    @Id
    val id: UUID = UUID.randomUUID(),
    val longLink: String,
    val shortLink: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
)
