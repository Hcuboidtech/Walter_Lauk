package com.walterlauk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewsData {

    @SerializedName("id")
    @Expose
    var id: Int? = 0

    @SerializedName("news_title")
    @Expose
    var news_title: String? = null

    @SerializedName("news_description")
    @Expose
    var news_description: String? = null

    @SerializedName("news_img")
    @Expose
    var news_img: String? = null

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null
}