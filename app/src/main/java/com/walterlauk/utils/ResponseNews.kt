package com.walterlauk.utils

data class ResponseNews(
    val `data`: List<NewsData>,
    val message: String,
    val status: Boolean
)