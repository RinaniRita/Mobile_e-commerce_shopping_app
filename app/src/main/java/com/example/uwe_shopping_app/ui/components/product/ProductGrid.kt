package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.domain.model.Product
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun ProductGrid(
    products: List<Product>,
    onProductClick: (Product) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductGridPreview() {
    Uwe_shopping_appTheme {
        val sampleProducts = listOf(
            Product(
                id = "1",
                name = "Sweater",
                price = 35.00,
                imageUrl = null
            ),
            Product(
                id = "2",
                name = "Long Sleeve Dress",
                price = 45.00,
                imageUrl = null
            ),
            Product(
                id = "3",
                name = "Sportwear Set",
                price = 80.00,
                imageUrl = null
            ),
            Product(
                id = "4",
                name = "Casual T-Shirt",
                price = 25.00,
                imageUrl = null
            ),
            Product(
                id = "5",
                name = "Denim Jacket",
                price = 65.00,
                imageUrl = null
            )
        )

        ProductGrid(products = sampleProducts)
    }
}
