package dkb.url_shortner

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
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
            .thenReturn(UrlMapping(longUrl = testUrl, shortUrl = testShortUrl))

        // when
        val result = service.shortenUrl(testUrl)

        //then
        verify(repository).findByLongUrl(testUrl)
        verify(repository).save(shortenedLinkCaptor.capture())

        assertEquals(testUrl, result.longUrl)
        assertEquals(testUrl, shortenedLinkCaptor.value.longUrl)
        assertTrue(shortenedLinkCaptor.value.shortUrl.matches(Regex("[a-zA-Z0-9]+")))
    }

    @Test
    fun `should return existing mapping when original url already exists`() {
        //given
        val urlMapping = UrlMapping(longUrl = testUrl, shortUrl = testShortUrl)
        `when`(repository.findByLongUrl(testUrl)).thenReturn(urlMapping)

        //when
        val result = service.shortenUrl(testUrl)

        //then
        verify(repository).findByLongUrl(testUrl)
        verify(repository, never()).save(any())

        assertEquals(urlMapping, result)
    }

    @Test
    fun `should return original url`() {
        //given
        val urlMapping = UrlMapping(longUrl = testUrl, shortUrl = testShortUrl)
        `when`(repository.findByShortUrl(testShortUrl)).thenReturn(urlMapping)

        //when
        val result = service.getOriginalUrl(testShortUrl)

        //then
        verify(repository).findByShortUrl(testShortUrl)

        assertEquals(testUrl, result)
    }

    @Test
    fun `should throw exception when original url not found`() {
        //given
        `when`(repository.findByShortUrl(testShortUrl)).thenReturn(null)

        //when
        val exception = assertThrows(UrlNotFoundException::class.java) {
            service.getOriginalUrl(testShortUrl)
        }

        //then
        verify(repository).findByShortUrl(testShortUrl)
        assertEquals("Invalid short url", exception.message)

    }
}