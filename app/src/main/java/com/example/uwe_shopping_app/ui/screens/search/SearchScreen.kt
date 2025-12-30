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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.navigation.navigateToResult
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.Sidebar
import com.example.uwe_shopping_app.ui.components.common.TopAppBar
import com.example.uwe_shopping_app.ui.components.search.ProductFilterSidebar
import com.example.uwe_shopping_app.ui.screens.notification.NotificationViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchScreen(
    navController: NavHostController,
    currentRoute: String,
    onNavigate: (String) -> Unit = {},
    viewModel: SearchViewModel = viewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val uiState = viewModel.uiState
    var isSidebarOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val notificationViewModel: NotificationViewModel = viewModel()

    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    val notificationsEnabled by sessionManager.notificationsEnabled.collectAsState(initial = true)
    val userId by sessionManager.userId.collectAsState(initial = null)

    val unreadCount by remember(userId) {
        userId?.let { notificationViewModel.getUnreadCount(it) }
            ?: flowOf(0)
    }.collectAsState(initial = 0)



    Box(modifier = Modifier.fillMaxSize()) {

        // ================= MAIN CONTENT =================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            TopAppBar(
                title = "Discover",
                navController = navController,
                onMenuClick = { isSidebarOpen = true },
                isLoggedIn = isLoggedIn,
                notificationsEnabled = notificationsEnabled,
                unreadCount = unreadCount,
                userId = userId
            )


            // ================= SEARCH BAR =================
            SearchBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onClearSearch = {
                    viewModel.clearSearch()
                    keyboardController?.hide()
                },
                onSearch = {
                    val query = uiState.searchQuery.trim()
                    if (query.isNotBlank()) {
                        keyboardController?.hide()
                        viewModel.submitSearch()

                        navController.navigateToResult(
                            query = query,
                            min = uiState.filterState.minPrice,
                            max = uiState.filterState.maxPrice,
                            sort = uiState.filterState.sortBy
                        )
                    }
                },
                onFilterClick = viewModel::showFilter
            )

            // ================= SUGGESTIONS =================
            if (uiState.suggestions.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
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
                                            min = uiState.filterState.minPrice,
                                            max = uiState.filterState.maxPrice,
                                            sort = uiState.filterState.sortBy
                                        )
                                    }
                                    .padding(16.dp)
                            )
                            Divider()
                        }
                    }
                }
            }

            // ================= DISCOVERY CONTENT =================
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(Modifier.height(16.dp))

                CategoryCardsSection(
                    onCategoryClick = { category ->
                        viewModel.loadProductsByCategory(category)

                        navController.navigateToResult(
                            query = category,
                            min = uiState.filterState.minPrice,
                            max = uiState.filterState.maxPrice,
                            sort = uiState.filterState.sortBy
                        )
                    }
                )

                Spacer(Modifier.height(80.dp))
            }

            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        // ================= BACKDROP =================
        if (uiState.showFilter) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { viewModel.hideFilter() }
            )
        }

        // ================= FILTER SIDEBAR =================
        ProductFilterSidebar(
            visible = uiState.showFilter,
            state = uiState.filterState,
            onStateChange = viewModel::updateFilterState,
            onReset = viewModel::resetFilter,
            onDismiss = viewModel::hideFilter,
            onApply = viewModel::applyFilter
        )
    }
    Sidebar(
        isOpen = isSidebarOpen,
        onClose = { isSidebarOpen = false },
        navController = navController,
        modifier = Modifier.zIndex(10f)
    )

}


/* ============================================================
   SEARCH BAR
   ============================================================ */

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
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = onClearSearch) {
                        Icon(Icons.Default.Close, null)
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            shape = RoundedCornerShape(12.dp)
        )

        Surface(
            onClick = onFilterClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(48.dp),
            color = Color(0xFFF5F5F5)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.MoreVert, contentDescription = "Filter")
            }
        }
    }
}


@Composable
private fun CategoryCardsSection(
    onCategoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        CategoryCard(
            title = "Electronics",
            subtitle = "Latest gadgets",
            backgroundColor = Color(0xFF5A7A8C),
            imageResId = R.drawable.elec_phone,
            onClick = { onCategoryClick("Electronics") }
        )

        CategoryCard(
            title = "Fashion",
            subtitle = "Trending styles",
            backgroundColor = Color(0xFFB5A99F),
            imageResId = R.drawable.fash_tshirt,
            onClick = { onCategoryClick("Fashion") }
        )

        CategoryCard(
            title = "Home",
            subtitle = "For your living space",
            backgroundColor = Color(0xFFC4A8B8),
            imageResId = R.drawable.home_lamp,
            onClick = { onCategoryClick("Home") }
        )

        CategoryCard(
            title = "Beauty",
            subtitle = "Care & cosmetics",
            backgroundColor = Color(0xFFE2A6B5),
            imageResId = R.drawable.beauty_serum,
            onClick = { onCategoryClick("Beauty") }
        )

        CategoryCard(
            title = "Sports",
            subtitle = "Fitness essentials",
            backgroundColor = Color(0xFF8FAFA0),
            imageResId = R.drawable.sport_yoga,
            onClick = { onCategoryClick("Sports") }
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



