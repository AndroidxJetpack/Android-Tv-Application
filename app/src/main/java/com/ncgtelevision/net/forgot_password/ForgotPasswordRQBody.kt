package com.ncgtelevision.net.forgot_password

import com.google.gson.annotations.SerializedName

class ForgotPasswordRQBody {
    @SerializedName("email")
    var email: String? = null
}