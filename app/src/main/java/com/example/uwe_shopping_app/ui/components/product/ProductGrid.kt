package com.example.uwe_shopping_app.ui.components.product

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uwe_shopping_app.data.local.entity.ProductEntity
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun ProductGrid(
    products: List<ProductEntity>,
    onProductClick: (ProductEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product) }
            )
        }
    }
    Log.d("HOME_VM", "Products size = ${products.size}")

}

//@Preview(showBackground = true)
//@Composable
//private fun ProductGridPreview() {
//    Uwe_shopping_appTheme {
//        ProductGrid(
//            products = listOf(
//                ProductEntity(
//                    id = 1,
//                    name = "Sweater",
//                    description = "Warm sweater",
//                    price = 35.00,
//                    imageResId = android.R.drawable.ic_menu_gallery,
//                    stock = 10,
//                    category = "Clothing"
//                ),
//                ProductEntity(
//                    id = 2,
//                    name = "Dress",
//                    description = "Summer dress",
//                    price = 45.00,
//                    imageResId = android.R.drawable.ic_menu_gallery,
//                    stock = 5,
//                    category = "Clothing"
//                )
//            )
//        )
//    }
//}
