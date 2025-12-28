package com.example.uwe_shopping_app.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uwe_shopping_app.data.chatbot.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val initialMessage =
        ChatMessage("Hello! Can I help you?", false)

    private val _messages = MutableStateFlow(listOf(initialMessage))
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun sendMessage(text: String) {
        _messages.value += ChatMessage(text, true)

        viewModelScope.launch {
            val reply = repository.getReply(text)
            _messages.value += ChatMessage(reply, false)
        }
    }

    fun clearChat() {
        _messages.value = listOf(initialMessage)
    }
}
