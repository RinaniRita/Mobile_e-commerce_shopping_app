package com.example.uwe_shopping_app.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShopStatusTabs(
    selectedTab: ShopTab,
    onTabSelected: (ShopTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = ShopTab.values()

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(40.dp)
            .border(
                width = 1.dp,
                color = Color(0xFF424242),
                shape = RoundedCornerShape(50)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onTabSelected(tab) }
                    .background(
                        color = if (isSelected) Color(0xFF424242) else Color.Transparent,
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    color = if (isSelected) Color.White else Color(0xFF424242),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
