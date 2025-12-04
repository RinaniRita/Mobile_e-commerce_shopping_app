package com.example.uwe_shopping_app.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.TopAppBar
import com.example.uwe_shopping_app.ui.components.product.VerticalProductGrid
import com.example.uwe_shopping_app.domain.model.Product
import com.example.uwe_shopping_app.ui.screens.home.CategoryChipsRow
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

@Composable
fun SearchScreen(
    currentRoute: String = "search",
    onNavigate: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Frontend-only mock results: typing "shirt" will reveal example products
    val searchResults = remember(searchQuery) {
        if (searchQuery.equals("shirt", ignoreCase = true)) {
            listOf(
                Product(
                    id = "shirt_1",
                    name = "Cotton Casual Shirt",
                    price = 32.00
                ),
                Product(
                    id = "shirt_2",
                    name = "Linen Oversized Shirt",
                    price = 48.00
                ),
                Product(
                    id = "shirt_3",
                    name = "Striped Summer Shirt",
                    price = 39.00
                ),
                Product(
                    id = "shirt_4",
                    name = "Classic White Shirt",
                    price = 29.00
                )
            )
        } else {
            emptyList()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(title = "Discover")

            // Search Bar Section
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    searchQuery = query
                },
                onClearSearch = {
                    searchQuery = ""
                    keyboardController?.hide()
                }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (searchQuery.isBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Show category chips when no search query
                        Spacer(modifier = Modifier.height(8.dp))
                        CategoryChipsRow()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Show category/collection cards
                        CategoryCardsSection()
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                } else {
                    if (searchResults.isEmpty()) {
                        // No results found
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No products found",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            SearchResultsHeader(
                                resultsCount = searchResults.size,
                                query = searchQuery
                            )
                            VerticalProductGrid(
                                products = searchResults,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemClick = onNavigate,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onFilterClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search TextField
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = {
                Text(
                    text = "Search",
                    color = Color(0xFF9E9E9E)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF9E9E9E)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = onClearSearch) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color(0xFF9E9E9E)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE0E0E0),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Search is already handled by onValueChange
                }
            )
        )

        // Filter Button
        Surface(
            onClick = onFilterClick,
            modifier = Modifier
                .size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF5F5F5)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Filter",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchResultsHeader(
    resultsCount: Int,
    query: String,
    onFilterClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = if (query.isNotBlank()) "Results for \"$query\"" else "Results",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9E9E9E)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Found $resultsCount ${if (resultsCount == 1) "item" else "items"}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )
        }

        Surface(
            onClick = onFilterClick,
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 0.dp,
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Filter",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Filter",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun CategoryCardsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 1: Women's Collection (Light Olive Green/Sage)
        CategoryCard(
            title = "Women's Collection",
            subtitle = "New Arrivals",
            backgroundColor = Color(0xFFB8C5A0), // Light olive green/sage
            imageResId = R.drawable.welcome_img,
            onClick = {}
        )

        // Card 2: Accessories (Medium Gray/Taupe)
        CategoryCard(
            title = "Accessories",
            subtitle = "Trending Now",
            backgroundColor = Color(0xFFB5A99F), // Medium gray/taupe
            imageResId = R.drawable.welcome_img,
            onClick = {}
        )

        // Card 3: Footwear (Dark Teal/Slate Blue)
        CategoryCard(
            title = "Footwear",
            subtitle = "Premium Collection",
            backgroundColor = Color(0xFF5A7A8C), // Dark teal/slate blue
            imageResId = R.drawable.welcome_img,
            onClick = {}
        )

        // Card 4: Outerwear (Light Lavender/Mauve)
        CategoryCard(
            title = "Outerwear",
            subtitle = "Style & Comfort",
            backgroundColor = Color(0xFFC4A8B8), // Light lavender/mauve
            imageResId = R.drawable.welcome_img,
            onClick = {}
        )
    }
}

@Composable
private fun CategoryCard(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    imageResId: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side: Text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            // Right side: Image with circular overlay
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(140.dp),
                contentAlignment = Alignment.Center
            ) {
                // Circular overlay background
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                )

                // Product image
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = title,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    Uwe_shopping_appTheme {
        SearchScreen()
    }
}

