package com.example.uwe_shopping_app.data.chatbot

import com.example.uwe_shopping_app.data.local.repository.ProductRepository

class ChatRepository(
    productRepository: ProductRepository,
    isLoggedIn: Boolean
) {
    private val engine = LocalChatEngine(
        productRepository = productRepository,
        isLoggedIn = isLoggedIn
    )

    suspend fun getReply(userInput: String): String {
        return engine.reply(userInput)
    }
}