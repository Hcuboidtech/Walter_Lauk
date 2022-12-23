package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TruckTrailerId {

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("number")
    @Expose
    var number:String?=null

    @SerializedName("type")
    @Expose
    var type:String?=null

    @SerializedName("service_date")
    @Expose
    var service_date:String?=null

    @SerializedName("safety_date")
    @Expose
    var safety_date:String?=null

    @SerializedName("created_at")
    @Expose
    var created_at:String?=null

    @SerializedName("updated_at")
    @Expose
    var updated_at:String?=null
}