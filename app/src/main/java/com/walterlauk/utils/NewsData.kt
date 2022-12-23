package com.walterlauk.utils

data class NewsData(
    val created_at: String,
    val id: Int,
    val news_description: String,
    val news_description_german: String,
    val news_description_russian: String,
    val news_description_serbian: String,
    val news_img: String,
    val news_title: String,
    val news_title_german: String,
    val news_title_russian: String,
    val news_title_serbian: String,
    val select_type: String,
    val updated_at: String
)