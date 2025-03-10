package dkb.url_shortner

import java.lang.RuntimeException

class UrlNotFoundException (message: String) : RuntimeException(message)