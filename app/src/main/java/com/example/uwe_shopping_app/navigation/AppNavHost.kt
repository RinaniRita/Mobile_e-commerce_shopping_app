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
import com.example.uwe_shopping_app.ui.screens.resultSearch.ResultSearchViewModel
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
import com.example.uwe_shopping_app.ui.screens.checkout.CheckoutViewModel
import com.example.uwe_shopping_app.ui.screens.voucher.VoucherScreen
import com.example.uwe_shopping_app.ui.screens.order.OrderScreen
import com.example.uwe_shopping_app.ui.screens.orderInfo.OrderInfoScreen
import com.example.uwe_shopping_app.ui.components.common.ShopTab
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
                val resViewModel: ResultSearchViewModel = viewModel()
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
                    onBack = { navController.popBackStack() },
                    viewModel = resViewModel
                )
            }

            composable(
                route = "product/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                ProductScreen(
                    productId = productId, 
                    onBack = { navController.popBackStack() },
                    navController = navController
                )
            }

            composable("cart") {
                CartScreen(navController = navController, currentRoute = "cart")
            }

            composable(
                route = "checkout?totalPrice={totalPrice}",
                arguments = listOf(navArgument("totalPrice") { type = NavType.StringType; defaultValue = "0.0" })
            ) { backStackEntry ->
                val totalPrice = backStackEntry.arguments?.getString("totalPrice")?.toDoubleOrNull() ?: 0.0
                val selectedAddress = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<AddressUiModel>("selected_address")

                val checkoutViewModel: CheckoutViewModel = viewModel()
                
                LaunchedEffect(totalPrice) {
                    checkoutViewModel.updateTotalPrice(totalPrice)
                }

                LaunchedEffect(selectedAddress) {
                    selectedAddress?.let {
                        checkoutViewModel.updateFromAddress(it)
                    }
                }

                CheckoutScreen(navController = navController, viewModel = checkoutViewModel)
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
                val addressToEdit = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<AddressUiModel>("address_to_edit")
                
                val addressViewModel: AddressViewModel = viewModel()

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
                route = "checkout_payment?productPrice={productPrice}&shippingPrice={shippingPrice}&shippingLabel={shippingLabel}&address={address}&phone={phone}&discount={discount}",
                arguments = listOf(
                    navArgument("productPrice") { type = NavType.StringType; defaultValue = "0.0" },
                    navArgument("shippingPrice") { type = NavType.StringType; defaultValue = "0.0" },
                    navArgument("shippingLabel") { type = NavType.StringType; defaultValue = "Free shipping" },
                    navArgument("address") { type = NavType.StringType; defaultValue = "" },
                    navArgument("phone") { type = NavType.StringType; defaultValue = "" },
                    navArgument("discount") { type = NavType.StringType; defaultValue = "0.0" }
                )
            ) { backStackEntry ->
                val productPrice = backStackEntry.arguments?.getString("productPrice")?.toDoubleOrNull() ?: 0.0
                val shippingPrice = backStackEntry.arguments?.getString("shippingPrice")?.toDoubleOrNull() ?: 0.0
                val shippingLabel = backStackEntry.arguments?.getString("shippingLabel") ?: "Free shipping"
                val address = backStackEntry.arguments?.getString("address") ?: ""
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                val discount = backStackEntry.arguments?.getString("discount")?.toDoubleOrNull() ?: 0.0
                
                CheckoutPaymentScreen(
                    navController = navController,
                    productPrice = productPrice,
                    shippingPrice = shippingPrice,
                    shippingLabel = shippingLabel,
                    address = address,
                    phone = phone,
                    discount = discount
                )
            }

            composable("checkout_completed") { CheckoutCompletedScreen(navController = navController) }

            composable(
                route = "orders?status={status}",
                arguments = listOf(navArgument("status") { defaultValue = "ON_THE_WAY" })
            ) { backStackEntry ->
                val statusStr = backStackEntry.arguments?.getString("status") ?: "ON_THE_WAY"
                val initialTab = try { ShopTab.valueOf(statusStr) } catch (e: Exception) { ShopTab.ON_THE_WAY }
                
                OrderScreen(
                    navController = navController,
                    currentRoute = "orders",
                    initialTab = initialTab
                )
            }

            composable(
                route = "orderInfo/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.IntType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
                OrderInfoScreen(
                    navController = navController,
                    orderId = orderId
                )
            }

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

            composable("voucher") {
                VoucherScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
