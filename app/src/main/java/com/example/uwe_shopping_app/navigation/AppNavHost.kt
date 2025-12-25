package com.example.uwe_shopping_app.navigation

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import com.example.uwe_shopping_app.ui.screens.onboarding.WelcomeScreen
import com.example.uwe_shopping_app.ui.screens.home.HomeScreen
import com.example.uwe_shopping_app.ui.screens.product.ProductScreen
import com.example.uwe_shopping_app.ui.screens.search.SearchScreen
import com.example.uwe_shopping_app.ui.screens.resultSearch.ResultSearchScreen
import com.example.uwe_shopping_app.ui.screens.cart.CartScreen
import com.example.uwe_shopping_app.ui.screens.checkout.CheckoutScreen
import com.example.uwe_shopping_app.ui.screens.checkout.CheckoutPaymentScreen
import com.example.uwe_shopping_app.ui.screens.checkout.CheckoutCompletedScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileSetting
import com.example.uwe_shopping_app.ui.screens.address.AddressScreen
import com.example.uwe_shopping_app.ui.screens.address.AddressControl
import com.example.uwe_shopping_app.ui.screens.address.AddressViewModel
import com.example.uwe_shopping_app.ui.screens.auth.LoginScreen
import com.example.uwe_shopping_app.ui.screens.auth.SignUpScreen
import com.example.uwe_shopping_app.ui.screens.order.OrderInfoDeliveredScreen
import com.example.uwe_shopping_app.ui.screens.order.OrderInfoOnTheWayScreen
import com.example.uwe_shopping_app.ui.screens.checkout.CheckoutViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavHost(navController: NavHostController, app: Application) {

    val session = remember { SessionManager(app) }
    var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        session.isLoggedIn.collectLatest { isLoggedIn = it }
    }

    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn == true) "home" else "welcome"
        ) {
            composable("welcome") {
                WelcomeScreen(onGetStarted = {
                    navController.navigate("login") { popUpTo("welcome") { inclusive = true } }
                })
            }

            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }

            composable("signup") {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate("home") { popUpTo("signup") { inclusive = true } }
                    },
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }

            composable("home") {
                HomeScreen(
                    navController = navController,
                    currentRoute = "home",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                )
            }

            composable("search") {
                SearchScreen(
                    navController = navController,
                    currentRoute = "search",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                )
            }

            composable(
                route = "resultSearch/{query}",
                arguments = listOf(navArgument("query") { type = NavType.StringType })
            ) { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                ResultSearchScreen(
                    query = query,
                    navController = navController,
                    currentRoute = "search",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "product/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                ProductScreen(productId = productId, onBack = { navController.popBackStack() })
            }

            composable("cart") {
                CartScreen(navController = navController, currentRoute = "cart")
            }

            composable(
                route = "checkout?totalPrice={totalPrice}",
                arguments = listOf(navArgument("totalPrice") { type = NavType.StringType; defaultValue = "0.0" })
            ) { backStackEntry ->
                val totalPrice = backStackEntry.arguments?.getString("totalPrice")?.toDoubleOrNull() ?: 0.0
                val viewModel: CheckoutViewModel = viewModel()
                
                LaunchedEffect(totalPrice) {
                    viewModel.updateTotalPrice(totalPrice)
                }

                CheckoutScreen(navController = navController, viewModel = viewModel)
            }

            composable(
                route = "address?from={from}&totalPrice={totalPrice}",
                arguments = listOf(
                    navArgument("from") { defaultValue = "checkout" },
                    navArgument("totalPrice") { defaultValue = "0.0" }
                )
            ) { backStackEntry ->
                val from = backStackEntry.arguments?.getString("from")
                val totalPrice = backStackEntry.arguments?.getString("totalPrice") ?: "0.0"
                val addressViewModel: AddressViewModel = viewModel()
                
                AddressScreen(
                    viewModel = addressViewModel,
                    onBackClick = { navController.popBackStack() },
                    onAddNewClick = { 
                        navController.currentBackStackEntry?.savedStateHandle?.remove<AddressUiModel>("address_to_edit")
                        navController.navigate("address_control") 
                    },
                    onEditClick = { address ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("address_to_edit", address)
                        navController.navigate("address_control")
                    },
                    onAddressSelected = { address ->
                        // ĐỒNG BỘ: Luôn gọi selectAddress để cập nhật DB
                        addressViewModel.selectAddress(address.id)

                        if (from != "profile") {
                            val prevRoute = navController.previousBackStackEntry?.destination?.route
                            if (prevRoute?.contains("checkout") == true) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("checkout?totalPrice=$totalPrice") {
                                    popUpTo("cart")
                                }
                            }
                        }
                    }
                )
            }

            composable("address_control") {
                val addressViewModel: AddressViewModel = viewModel()
                val addressToEdit = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<AddressUiModel>("address_to_edit")

                AddressControl(
                    addressToEdit = addressToEdit,
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { newAddress ->
                        addressViewModel.addOrUpdateAddress(newAddress)
                        navController.popBackStack()
                    },
                    onDeleteClick = { addressToDelete ->
                        addressViewModel.deleteAddress(addressToDelete)
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "checkout_payment?productPrice={productPrice}&shippingPrice={shippingPrice}&shippingLabel={shippingLabel}",
                arguments = listOf(
                    navArgument("productPrice") { type = NavType.StringType; defaultValue = "0.0" },
                    navArgument("shippingPrice") { type = NavType.StringType; defaultValue = "0.0" },
                    navArgument("shippingLabel") { type = NavType.StringType; defaultValue = "Free shipping" }
                )
            ) { backStackEntry ->
                val productPrice = backStackEntry.arguments?.getString("productPrice")?.toDoubleOrNull() ?: 0.0
                val shippingPrice = backStackEntry.arguments?.getString("shippingPrice")?.toDoubleOrNull() ?: 0.0
                val shippingLabel = backStackEntry.arguments?.getString("shippingLabel") ?: "Free shipping"
                
                CheckoutPaymentScreen(
                    navController = navController,
                    productPrice = productPrice,
                    shippingPrice = shippingPrice,
                    shippingLabel = shippingLabel
                )
            }

            composable("checkout_completed") { CheckoutCompletedScreen(navController = navController) }
            composable("orderInfoDelivered") { OrderInfoDeliveredScreen(navController = navController) }
            composable("orderInfoOnTheWay") { OrderInfoOnTheWayScreen(navController = navController) }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    currentRoute = "profile",
                    onNavigate = { route ->
                        if (route == "address") {
                            navController.navigate("address?from=profile")
                        } else {
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo("home") { saveState = true }
                            }
                        }
                    }
                )
            }

            composable("profile_setting") { ProfileSetting(onBack = { navController.popBackStack() }) }
        }
    }
}
