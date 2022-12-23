package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TeamData {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("main_role")
    @Expose
    var main_role: String? = null

    @SerializedName("team_image")
    @Expose
    var team_image: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("primary_no")
    @Expose
    var primary_no: String? = null

    @SerializedName("secondary_no")
    @Expose
    var secondary_no: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null
}