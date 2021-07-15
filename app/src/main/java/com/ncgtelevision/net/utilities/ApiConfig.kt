package com.ncgtelevision.net.utilities

class ApiConfig private constructor() {
    companion object {
        const val WEB_URL = "https://ncgtelevision.net"

        const val BASE_URL = WEB_URL + "/wp-json/jwt-auth/v2/"
        const val WITHOUT_AUTH_BASE_URL = WEB_URL + "/wp-json/wp/v2/"
    }

    init {
        throw IllegalStateException("Utility class")
    }
}