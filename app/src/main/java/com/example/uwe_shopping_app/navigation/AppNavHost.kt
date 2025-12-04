package com.example.uwe_shopping_app.navigation

import android.app.Application
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.screens.onboarding.WelcomeScreen
import com.example.uwe_shopping_app.ui.screens.home.HomeScreen
import com.example.uwe_shopping_app.ui.screens.search.SearchScreen
import com.example.uwe_shopping_app.ui.screens.cart.CartScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileScreen
import com.example.uwe_shopping_app.ui.screens.auth.LoginScreen
import com.example.uwe_shopping_app.ui.screens.auth.SignUpScreen
import com.example.uwe_shopping_app.ui.screens.resultSearch.ResultSearchScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavHost(navController: NavHostController, app: Application) {

    val session = remember { SessionManager(app) }
    var isLoggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        session.isLoggedIn.collectLatest { isLoggedIn = it }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "welcome"
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
            HomeScreen(
                onNavigate = { route ->
                    if (route == "profile") {
                        if (isLoggedIn) {
                            navController.navigate("profile")
                        } else {
                            navController.navigate("login")
                        }
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

        //  ========== Search ============
        composable("search") {
            SearchScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onNavigateToResults = { query ->
                    val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
                    navController.navigate("resultSearch/$encodedQuery")
                }
            )
        }

        composable(
            route = "resultSearch/{query}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedQuery = backStackEntry.arguments?.getString("query").orEmpty()
            val query = java.net.URLDecoder.decode(encodedQuery, "UTF-8")

            ResultSearchScreen(
                query = query,
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        // =========== Profile ===========
        composable("profile") { ProfileScreen() }
    }
}