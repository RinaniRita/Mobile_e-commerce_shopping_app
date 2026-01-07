package com.example.uwe_shopping_app.navigation

import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.search.SortOption
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavHostController.navigateToResult(
    query: String,
    min: Float = 0f,
    max: Float = 1500f,
    sort: SortOption = SortOption.NEWEST
) {
    val encodedQuery = URLEncoder.encode(
        query,
        StandardCharsets.UTF_8.toString()
    )

    navigate("resultSearch/$encodedQuery/$min/$max/${sort.name}") {
        launchSingleTop = true
    }
}