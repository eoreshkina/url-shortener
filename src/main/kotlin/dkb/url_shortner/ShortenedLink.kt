package dkb.url_shortner

import jakarta.persistence.Entity
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class ShortenedLink(
    val id: UUID,
    val long_link: String,
    val short_link: String,
    val created_at: ZonedDateTime
)
