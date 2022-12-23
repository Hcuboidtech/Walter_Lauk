package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTeams {

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = false

    @SerializedName("data")
    @Expose
    var data: ArrayList<Teams>? = null
}