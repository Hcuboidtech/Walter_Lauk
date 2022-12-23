package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
data class DataGetUserProfile(
    val created_at: String,
    val device_token: String,
    val device_type: String,
    val driver_phone_number: String,
    val driver_type: String,
    val email: Any,
    val email_verified_at: Any,
    val id: Int,
    val imageName: String,
    val image_url: String,
    val is_active: String,
    val is_admin: String,
    val name: String,
    val updated_at: String,
    val user_name: String
)*/
class DataGetUserProfile {
    @SerializedName("id")
    @Expose
    var id: Int? = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("email_verified_at")
    @Expose
    var email_verified_at: String? = null

    @SerializedName("driver_type")
    @Expose
    var driver_type: String? = null

    @SerializedName("driver_phone_number")
    @Expose
    var driver_phone_number: String? = null

    @SerializedName("is_admin")
    @Expose
    var is_admin: String? = null

    @SerializedName("is_active")
    @Expose
    var is_active: String? = null

    @SerializedName("image_url")
    @Expose
    var image_url: String? = null

    @SerializedName("user_name")
    @Expose
    var user_name: String? = null

    @SerializedName("device_type")
    @Expose
    var device_type: String? = null

    @SerializedName("device_token")
    @Expose
    var device_token: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null

    @SerializedName("imageName")
    @Expose
    var imageName: String? = null
}