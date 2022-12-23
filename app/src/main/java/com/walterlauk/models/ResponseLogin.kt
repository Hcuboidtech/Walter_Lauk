package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
data class ResponseLogin(
    val `data`: DataResponse,
    val message: String,
    val status: Boolean
)*/

class ResponseLogin {
    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("status")
    @Expose
    val status: Boolean? = false

    @SerializedName("data")
    @Expose
    val data: DataResponse? = null
}
