package dkb.url_shortner

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ShortenerServiceTest {
    @Mock
    private lateinit var repository: ShortenerRepository

    @InjectMocks
    private lateinit var service: ShortenerService

    @Captor
    private lateinit var shortenedLinkCaptor: ArgumentCaptor<UrlMapping>


    private val testUrl = "http://localhost:8080/test"
    private val testShortUrl = "test123"


    @Test
    fun `should create short url if not exists`() {
        // given
        `when`(repository.findByLongUrl(testUrl)).thenReturn(null)

        `when`(repository.save(any()))
            .thenReturn(UrlMapping(longLink = testUrl, shortLink = testShortUrl))

        // when
        val result = service.shortenUrl(testUrl)

        //then
        verify(repository).findByLongUrl(testUrl)
        verify(repository).save(shortenedLinkCaptor.capture())

        assertEquals(testUrl, result.longLink)
        assertEquals(testUrl, shortenedLinkCaptor.value.longLink)
        assertTrue(shortenedLinkCaptor.value.shortLink.matches(Regex("[a-zA-Z0-9]+")))
    }

    @Test
    fun `should return existing mapping when original url already exists`() {
        //given
        val urlMapping = UrlMapping(longLink = testUrl, shortLink = testShortUrl)
        `when`(repository.findByLongUrl(testUrl)).thenReturn(urlMapping)

        //when
        val result = service.shortenUrl(testUrl)

        //then
        verify(repository).findByLongUrl(testUrl)
        verify(repository, never()).save(any())

        assertEquals(urlMapping, result)
    }

}