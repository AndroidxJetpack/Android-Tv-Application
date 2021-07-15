package com.ncgtelevision.net.utilities

class ConstantUtility private constructor() {
    companion object {

        // Global variable used to store network state
        @JvmField
        var isNetworkConnected = false
        const val PREF_DATA = "pref_data"
        const val LOADING = "Loading..."
        const val AUTH_TOKEN = "token"
        const val INCORRECT_CREDENTIALS = "Incorrect Username or Password"
        const val SERVER_ISSUE = "Server issue, try after sometime"
        const val FILL_EMAIL = "Fill Email ID"
        const val EMAIL_VALIDATION = "Fill Valid Email ID"
        const val FILL_PASSW = "Fill Password"
        const val WRONG_EMAIL = "Wrong email, Please check your email Id"
        const val SESSION_EXPIRED = "Session expired, Please re-login"
        const val MAX_USER_LOGIN = "Your account is in use on 5 devices. Please stop playing on other devices to continue"
        const val CHECK_INTERNET_CONNECTION = "NO INTERNET, Please Check Your Internet Connection."
        const val EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    }

    init {
        throw IllegalStateException("Utility class")
    }
}