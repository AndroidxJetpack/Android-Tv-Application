package com.ncgtelevision.net.search_screen

import com.google.gson.annotations.SerializedName

class SearchRequestBody {
    @SerializedName("search_term")
    var search_keyword: String?=null
}