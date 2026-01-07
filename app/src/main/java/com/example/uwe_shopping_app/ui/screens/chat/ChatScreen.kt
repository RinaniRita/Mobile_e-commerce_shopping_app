package com.example.uwe_shopping_app.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.uwe_shopping_app.ui.components.chat.ChatBubble
import com.example.uwe_shopping_app.ui.components.chat.ChatInputBar
import com.example.uwe_shopping_app.ui.components.chat.ChatTopBar
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp


@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    Column {
        ChatTopBar(
            onBack = onBack,
            onClearChat = {
                viewModel.clearChat()
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { message ->
                ChatBubble(
                    message = message,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }

        ChatInputBar(
            text = input,
            onTextChange = { input = it },
            onSend = {
                if (input.isNotBlank()) {
                    viewModel.sendMessage(input)
                    input = ""
                }
            }
        )
    }
}
