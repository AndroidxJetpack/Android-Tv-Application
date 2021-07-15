package com.ncgtelevision.net.utilities

class NetworkKeys private constructor() {
    companion object {
        const val KEY_CONTENT_TYPE = "Accept: application/json"
        const val KEY_STATUS = "Status"
        const val KEY_AUTH = "Authorization"
        const val KEY_ACCEPT = "Accept"
        const val KEY_EMAIL = "email"
        const val KEY_PASS = "password"
        const val KEY_DEVICE_ID = "deviceid"
        const val KEY_BEARER = "Bearer "
        const val KEY_APP_JSON = "application/json"
        const val KEY_SUPPORT = "support"
    }

    init {
        throw IllegalStateException("Utility class")
    }
}