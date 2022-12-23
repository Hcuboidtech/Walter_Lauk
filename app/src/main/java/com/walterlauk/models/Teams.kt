package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Teams {

    @SerializedName("main_role")
    @Expose
    var main_role: String? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<TeamData>? = null
}