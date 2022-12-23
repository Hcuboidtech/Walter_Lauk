package com.walterlauk.models

data class ResponseSpecificNews(
    val `data`: SpecificNewsData,
    val message: String,
    val status: Boolean
)