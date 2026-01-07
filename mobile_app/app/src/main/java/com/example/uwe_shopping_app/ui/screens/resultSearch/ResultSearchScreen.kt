package com.example.uwe_shopping_app.ui.screens.resultSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.search.ProductFilterSidebar
import com.example.uwe_shopping_app.ui.components.product.VerticalProductGrid
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun ResultSearchScreen(
    query: String,
//    initialFilter: SearchFilterState,
    navController: NavHostController? = null,
    currentRoute: String = "search",
    onNavigate: (String) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: ResultSearchViewModel
) {
    val uiState = viewModel.uiState

    val decodedQuery = URLDecoder.decode(
        query,
        StandardCharsets.UTF_8.toString()
    )

    LaunchedEffect(decodedQuery) {
        viewModel.setQuery(decodedQuery)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            color = Color(0xFFF5F5F5)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Simple top bar with back button and title
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        }
                        Text(
                            text = "Results",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f)) {

                    when {
                        uiState.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        else -> {
                            Column(modifier = Modifier.fillMaxSize()) {

                                // ALWAYS show header (even when 0 results)
                                SearchResultsHeader(
                                    resultsCount = uiState.searchResults.size,
                                    query = uiState.query,
                                    onFilterClick = viewModel::showFilter
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                if (uiState.searchResults.isEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 48.dp),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        Text(
                                            text = "No products match your filters",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF9E9E9E)
                                        )
                                    }
                                } else {
                                    VerticalProductGrid(
                                        products = uiState.searchResults,
                                        modifier = Modifier.fillMaxSize(),
                                        onProductClick = { product ->
                                            navController?.navigate("product/${product.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }



                navController?.let {
                    BottomNavigationBar(
                        navController = it,
                        currentRoute = currentRoute
                    )
                }
            }
        }
    }

//                Filter
    ProductFilterSidebar(
        visible = uiState.showFilter,
        state = uiState.filterState,
        onStateChange = viewModel::updateFilterState,
        onReset = viewModel::resetFilter,
        onDismiss = viewModel::hideFilter,
        onApply = viewModel::applyFilter
    )
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
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                color = Color.Black
            )
        }

        Surface(
            onClick = onFilterClick,
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 0.dp,
            border = ButtonDefaults.outlinedButtonBorder(enabled = true)
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
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}



