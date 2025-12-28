package com.example.uwe_shopping_app.ui.screens.chat

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)


