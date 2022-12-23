package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*class ResponseGetUserProfile(
    val `data`: DataGetUserProfile,
    val message: String,
    val status: Boolean
)*/
class ResponseGetUserProfile {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: Boolean? = false

    @SerializedName("data")
    @Expose
    var data: DataGetUserProfile? = null
}