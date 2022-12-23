package com.walterlauk.models

data class DriverChatAdmin_123Response(
    val id: String ="",
    val latest_message: LatestMessage = LatestMessage(),
    val name: String="",
    val other_user_email: String=""
)