package com.walterlauk.utils

data class ResponseTruckTrailerParts(
    val `data`: TruckTrailerData,
    val message: String,
    val status: Boolean
)