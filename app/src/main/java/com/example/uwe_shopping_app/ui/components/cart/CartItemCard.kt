package com.example.uwe_shopping_app.ui.components.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

data class CartItemUiModel(
    val id: Int,
    val productId: Int,
    val name: String,
    val price: Double,
    val imageResId: Int,
    val size: String,
    val color: String,
    val quantity: Int,
    val isSelected: Boolean = true
)

@Composable
fun CartItemCard(
    item: CartItemUiModel,
    onToggleSelection: (Int) -> Unit,
    onQuantityDecrease: (Int) -> Unit,
    onQuantityIncrease: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            Image(
                painter = painterResource(
                    id = item.imageResId.takeIf { it != 0 }
                        ?: android.R.drawable.ic_menu_gallery
                ),
                contentDescription = item.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Product Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Product Name
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Price
                    Text(
                        text = "$${String.format(Locale.getDefault(), "%.2f", item.price)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Size and Color
                    Text(
                        text = "Size: ${item.size} | Color: ${item.color}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Quantity Selector
                QuantitySelector(
                    quantity = item.quantity,
                    onDecrease = { onQuantityDecrease(item.id) },
                    onIncrease = { onQuantityIncrease(item.id) },
                    modifier = Modifier.width(100.dp)
                )
            }

            // Checkbox
            Checkbox(
                checked = item.isSelected,
                onCheckedChange = { onToggleSelection(item.id) },
                modifier = Modifier.align(Alignment.Top)
            )
        }
    }
}

