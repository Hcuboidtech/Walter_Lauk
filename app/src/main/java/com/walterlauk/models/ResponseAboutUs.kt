package com.walterlauk.models

data class ResponseAboutUs(
    val `data`: DataAboutUs,
    val message: String,
    val status: Boolean
)