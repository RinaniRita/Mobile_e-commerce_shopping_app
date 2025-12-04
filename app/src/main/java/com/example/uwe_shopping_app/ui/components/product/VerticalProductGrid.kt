package com.example.uwe_shopping_app.ui.components.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.domain.model.Product
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

/**
 * Dedicated vertical grid for search/listing screens.
 */
@Composable
fun VerticalProductGrid(
    products: List<Product>,
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onProductClick(product) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun VerticalProductGridPreview() {
    Uwe_shopping_appTheme {
        VerticalProductGrid(
            products = listOf(
                Product(id = "1", name = "Linen Dress", price = 52.00),
                Product(id = "2", name = "Fitted Waist Dress", price = 47.99),
                Product(id = "3", name = "Maxi Dress", price = 68.00),
                Product(id = "4", name = "Front Tie Dress", price = 59.00)
            )
        )
    }
}

