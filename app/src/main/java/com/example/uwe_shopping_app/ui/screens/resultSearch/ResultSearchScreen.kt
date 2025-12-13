package com.example.uwe_shopping_app.ui.screens.resultSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.product.VerticalProductGrid
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun ResultSearchScreen(
    query: String,
    navController: NavHostController? = null,
    currentRoute: String = "search",
    onNavigate: (String) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: ResultSearchViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    val decodedQuery = URLDecoder.decode(
        query,
        StandardCharsets.UTF_8.toString()
    )


    LaunchedEffect(query) {
        val decodedQuery = URLDecoder.decode(
            query,
            StandardCharsets.UTF_8.toString()
        )
        viewModel.search(decodedQuery)
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
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "Results",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.searchResults.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No products found for \"$query\"",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }

                    else -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            SearchResultsHeader(
                                resultsCount = uiState.searchResults.size,
                                query = uiState.query
                            )
                            VerticalProductGrid(
                                products = uiState.searchResults,
                                modifier = Modifier.weight(1f),
                                onProductClick = { product ->
                                    navController?.navigate("product/${product.id}")
                                }
                            )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ResultSearchScreenPreview() {
    Uwe_shopping_appTheme {
        ResultSearchScreen(query = "Dress")
    }
}


