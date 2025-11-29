package com.example.uwe_shopping_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uwe_shopping_app.ui.screens.onboarding.WelcomeScreen
import com.example.uwe_shopping_app.ui.screens.home.HomeScreen
import com.example.uwe_shopping_app.ui.screens.cart.CartScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileScreen
import com.example.uwe_shopping_app.ui.screens.auth.LoginScreen
import com.example.uwe_shopping_app.ui.screens.auth.SignUpScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "welcome"
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

//        // =========== Auth ===========
//        composable("login") {
//            LoginScreen(
//                onLoginSuccess = { navController.navigate("home") }
//            )
//        }
//
//        composable("signup") {
//            SignUpScreen(
//                onSignUpSuccess = { navController.navigate("home") }
//            )
//        }
    }
}