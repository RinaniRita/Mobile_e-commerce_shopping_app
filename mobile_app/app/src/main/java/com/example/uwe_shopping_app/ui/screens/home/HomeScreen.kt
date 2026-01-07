package com.example.uwe_shopping_app.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.ui.components.common.BottomNavigationBar
import com.example.uwe_shopping_app.ui.components.common.Sidebar
import com.example.uwe_shopping_app.ui.components.common.TopAppBar
import com.example.uwe_shopping_app.ui.components.product.ProductGrid
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import androidx.navigation.NavHostController
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.screens.notification.NotificationViewModel
import kotlinx.coroutines.flow.flowOf


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    currentRoute: String,
    onNavigate: (String) -> Unit = {}
) {
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            color = Color(0xFFF5F5F5)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopAppBar(
                    onMenuClick = { isSidebarOpen = true },
                    navController = navController,
                    isLoggedIn = isLoggedIn,
                    notificationsEnabled = notificationsEnabled,
                    unreadCount = unreadCount,
                    userId = userId
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                // Main scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    HeroBanner()

                    Spacer(modifier = Modifier.height(16.dp))

                    SectionHeader(title = "Feature Products")
                    ProductGrid(
                        products = uiState.featuredProducts,
                        onProductClick = { product ->
                            navController.navigate("product/${product.id}")
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HighlightCard(
                        title = "NEW COLLECTION",
                        subtitle = "HANG OUT & PARTY"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SectionHeader(title = "Recommended")
                    ProductGrid(
                        products = uiState.recommendedProducts,
                        onProductClick = { product ->
                            navController.navigate("product/${product.id}")
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SectionHeader(title = "Top Collection")
                    ProductGrid(
                        products = uiState.topCollectionProducts,
                        onProductClick = { product ->
                            navController.navigate("product/${product.id}")
                        }
                    )

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                )
            }
        }

        // Sidebar
        Sidebar(
            isOpen = isSidebarOpen,
            onClose = { isSidebarOpen = false },
            navController = navController,
            modifier = Modifier.zIndex(10f)
        )
    }
}

@Composable
private fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFFE0B2),
                        Color(0xFFFFCCBC)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome_img),
            contentDescription = "Autumn Collection",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = "Autumn Collection",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = Color.White
            )
            Text(
                text = "2022",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )
        Text(
            text = "Show all",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF9E9E9E)
        )
    }
}

@Composable
private fun HighlightCard(
    title: String,
    subtitle: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF9E9E9E)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color(0xFFFFE0B2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "★",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
