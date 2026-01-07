package com.example.uwe_shopping_app.ui.components.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProductFilterSidebar(
    visible: Boolean,
    state: SearchFilterState,
    onStateChange: (SearchFilterState) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Backdrop
        if (visible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { onDismiss() }
            )
        }

        // RIGHT SIDEBAR
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally { fullWidth -> fullWidth },   // from right
            exit = slideOutHorizontally { fullWidth -> fullWidth },  // to right
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Surface(
                modifier = Modifier
                    .width(320.dp)
                    .fillMaxHeight(),
                shadowElevation = 16.dp
            ) {
                SearchFilterSheet(
                    state = state,
                    onStateChange = onStateChange,
                    onReset = onReset,
                    onApply = onApply
                )
            }
        }
    }
}


