package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
data class DataResponse(
    val authorization_token: String,
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
class DataResponse {
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

    @SerializedName("email_verified_at")
    @Expose
    val email_verified_at: String? = null

    @SerializedName("driver_type")
    @Expose
    val driver_type: String? = null

    @SerializedName("driver_phone_number")
    @Expose
    val driver_phone_number: String? = null

    @SerializedName("is_admin")
    @Expose
    val is_admin: String? = null

    @SerializedName("is_active")
    @Expose
    val is_active: String? = null

    @SerializedName("image_url")
    @Expose
    val image_url: String? = null

    @SerializedName("user_name")
    @Expose
    val user_name: String? = null

    @SerializedName("device_type")
    @Expose
    val device_type: String? = null

    @SerializedName("device_token")
    @Expose
    val device_token: String? = null

    @SerializedName("created_at")
    @Expose
    val created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    val updated_at: String? = null

    @SerializedName("imageName")
    @Expose
    val imageName: String? = null

    @SerializedName("authorization_token")
    @Expose
    val authorization_token: String? = null
}
