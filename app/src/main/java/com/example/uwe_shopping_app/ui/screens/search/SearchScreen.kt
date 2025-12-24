package com.example.uwe_shopping_app.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.TopAppBar
import com.example.uwe_shopping_app.ui.screens.home.CategoryChipsRow
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.uwe_shopping_app.navigation.navigateToResult
import com.example.uwe_shopping_app.ui.components.product.ProductFilterSidebar
import java.net.URLEncoder
import java.nio.charset.StandardCharsets




@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    navController: NavHostController,
    currentRoute: String,
    onNavigate: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState = viewModel.uiState
    val searchQuery = uiState.searchQuery
    var filterState by remember { mutableStateOf(SearchFilterState()) }
    var showFilter by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {

        // MAIN CONTENT
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            color = Color(0xFFF5F5F5)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                TopAppBar(title = "Discover")

                // Search Bar
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onClearSearch = {
                        viewModel.clearSearch()
                        keyboardController?.hide()
                    },
                    onSearch = {
                        val trimmed = searchQuery.trim()
                        if (trimmed.isNotBlank()) {
                            keyboardController?.hide()
                            viewModel.submitSearch()

                            val encodedQuery = URLEncoder.encode(
                                trimmed,
                                StandardCharsets.UTF_8.toString()
                            )

                            //  DEFAULT FILTER VALUES
                            val min = 0f
                            val max = 1500f
                            val sort = SortOption.NEWEST.name

                            navController.navigateToResult(
                                query = trimmed,
                                min = filterState.minPrice,
                                max = filterState.maxPrice,
                                sort = filterState.sortBy
                            )
                        }
                    },
                    onFilterClick = { showFilter = true }
                )


                // Suggestions
                if (uiState.suggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            uiState.suggestions.forEach { suggestion ->
                                Text(
                                    text = suggestion,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.updateSearchQuery(suggestion)
                                            viewModel.submitSearch()
                                            keyboardController?.hide()


                                            navController.navigateToResult(
                                                query = suggestion,
                                                min = filterState.minPrice,
                                                max = filterState.maxPrice,
                                                sort = filterState.sortBy
                                            )

                                        }
                                        .padding(16.dp)
                                )
                                Divider()
                            }
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CategoryChipsRow()
                        Spacer(modifier = Modifier.height(16.dp))
                        CategoryCardsSection()
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }

                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }

        // BACKDROP (click to close)
        if (showFilter) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { showFilter = false }
            )
        }

        // RIGHT SIDEBAR
        ProductFilterSidebar(
            visible = showFilter,
            state = filterState,
            onStateChange = { filterState = it },
            onReset = { filterState = SearchFilterState() },
            onDismiss = { showFilter = false },
            onApply = {
                showFilter = false
                viewModel.applyFilter(
                    filterState.minPrice,
                    filterState.maxPrice,
                    filterState.sortBy
                )
            }
        )
    }
}


@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSearch: () -> Unit,
    onFilterClick: () -> Unit
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
                    onSearch() }
            )
        )

        // Filter Button (RIGHT side)
        Surface(
            onClick = onFilterClick,
            modifier = Modifier.size(48.dp),
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

data class SearchFilterState(
    val minPrice: Float = 0f,
    val maxPrice: Float = 1500f,
    val sortBy: SortOption = SortOption.NEWEST
)

enum class SortOption {
    NEWEST,
    OLDEST,
    NAME_ASC,
    NAME_DESC
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterSheet(
    state: SearchFilterState,
    onStateChange: (SearchFilterState) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text("Filter", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Price", fontWeight = FontWeight.SemiBold)

        RangeSlider(
            value = state.minPrice..state.maxPrice,
            valueRange = 0f..1500f,
            onValueChange = {
                onStateChange(
                    state.copy(
                        minPrice = it.start,
                        maxPrice = it.endInclusive
                    )
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$${state.minPrice.toInt()}")
            Text("$${state.maxPrice.toInt()}")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Sort By", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))

        SortChip("Newest", state.sortBy == SortOption.NEWEST) {
            onStateChange(state.copy(sortBy = SortOption.NEWEST))
        }

        Spacer(modifier = Modifier.height(8.dp))

        SortChip("Oldest", state.sortBy == SortOption.OLDEST) {
            onStateChange(state.copy(sortBy = SortOption.OLDEST))
        }

        Spacer(modifier = Modifier.height(8.dp))

        SortChip("A → Z", state.sortBy == SortOption.NAME_ASC) {
            onStateChange(state.copy(sortBy = SortOption.NAME_ASC))
        }

        Spacer(modifier = Modifier.height(8.dp))

        SortChip("Z → A", state.sortBy == SortOption.NAME_DESC) {
            onStateChange(state.copy(sortBy = SortOption.NAME_DESC))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onReset) {
                Text("Reset")
            }

            Button(
                onClick = onApply,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Apply")
            }
        }
    }
}

@Composable
private fun SortChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = if (selected) Color.Black else Color(0xFFF2F2F2)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (selected) Color.White else Color.Black
            )
        }
    }
}





