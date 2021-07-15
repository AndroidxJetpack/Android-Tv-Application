package com.ncgtelevision.net.forgot_password

import com.google.gson.annotations.SerializedName

class ForgotPasswordModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: Boolean? = null
}