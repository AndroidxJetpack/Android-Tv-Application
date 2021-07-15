package com.ncgtelevision.net.signin

import com.google.gson.annotations.SerializedName

class SendSignInBody {
    @SerializedName("password")
    var password: String? = null

    @SerializedName("username")
    var username: String? = null
}