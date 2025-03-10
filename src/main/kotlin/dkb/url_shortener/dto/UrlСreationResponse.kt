package dkb.url_shortener.dto

import java.util.UUID

data class UrlCreationResponse(
    val id: UUID,
    val shortUrl: String,
    val createdAt: String
)
