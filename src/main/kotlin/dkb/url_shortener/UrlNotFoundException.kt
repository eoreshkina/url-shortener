package dkb.url_shortener

import java.lang.RuntimeException

class UrlNotFoundException (message: String) : RuntimeException(message)