package com.example.uwe_shopping_app.navigation

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.screens.onboarding.WelcomeScreen
import com.example.uwe_shopping_app.ui.screens.home.HomeScreen
import com.example.uwe_shopping_app.ui.screens.search.SearchScreen
import com.example.uwe_shopping_app.ui.screens.resultSearch.ResultSearchScreen
import com.example.uwe_shopping_app.ui.screens.cart.CartScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileSetting
import com.example.uwe_shopping_app.ui.screens.auth.LoginScreen
import com.example.uwe_shopping_app.ui.screens.auth.SignUpScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavHost(navController: NavHostController, app: Application) {

    val session = remember { SessionManager(app) }

    var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        session.isLoggedIn.collectLatest { isLoggedIn = it }
    }

    if (isLoggedIn == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn == true) "home" else "welcome"
        ) {

            // =========== Onboarding ===========
            composable("welcome") {
                WelcomeScreen(
                    onGetStarted = {
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )
            }

            // =========== Auth ===========
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }

            composable("signup") {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate("home") {
                            popUpTo("signup") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }

            // =========== Home ===========
            composable("home") {
                val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home"

                HomeScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                )
            }

            //  ========== Search ============
            composable("search") {
                val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "search"

                SearchScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                )
            }

            //  ========== Result Search ============
            composable(
                route = "resultSearch/{query}",
                arguments = listOf(
                    navArgument("query") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "search"

                ResultSearchScreen(
                    query = query,
                    navController = navController,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }


            //  ========== Cart ============
            composable("cart") {
                val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "cart"

                CartScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                )
            }

            //  ========== Profile ============
            composable("profile") {
                val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "profile"

                ProfileScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                )
            }

            // =========== Profile Setting ===========
            composable("profile_setting") {
                ProfileSetting(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}