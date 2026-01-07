package com.example.uwe_shopping_app.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.ui.screens.chat.ChatMessage

@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val bubbleShape = if (message.isUser) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 4.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 4.dp,
            bottomEnd = 16.dp
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement =
            if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = bubbleShape,
            tonalElevation = 1.dp,
            color = if (message.isUser)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = message.text,
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 280.dp),
                color = if (message.isUser)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
