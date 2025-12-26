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
import com.example.uwe_shopping_app.ui.components.common.ShopTab
import com.example.uwe_shopping_app.ui.components.product.SearchFilterState
import com.example.uwe_shopping_app.ui.components.product.SortOption
import com.example.uwe_shopping_app.ui.screens.address.AddressControl
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
import com.example.uwe_shopping_app.ui.screens.address.AddressViewModel
import com.example.uwe_shopping_app.ui.screens.voucher.VoucherScreen
import com.example.uwe_shopping_app.ui.screens.auth.LoginScreen
import com.example.uwe_shopping_app.ui.screens.auth.SignUpScreen
import com.example.uwe_shopping_app.ui.screens.checkout.CheckoutViewModel
import com.example.uwe_shopping_app.ui.screens.order.OrderScreen
import com.example.uwe_shopping_app.ui.screens.orderInfo.OrderInfoScreen
import com.example.uwe_shopping_app.ui.screens.orderInfo.OrderInfoViewModel
import com.example.uwe_shopping_app.ui.screens.resultSearch.ResultSearchViewModel
import com.example.uwe_shopping_app.ui.screens.review.ReviewScreen

import kotlinx.coroutines.flow.collectLatest
@Composable
fun AppNavHost(
    navController: NavHostController,
    app: Application
) {
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
        return
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn == true) "home" else "welcome"
    ) {

        // ---------------- Onboarding ----------------
        composable("welcome") {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // ---------------- Auth ----------------
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

        // ---------------- Home (PUBLIC) ----------------
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

        // ---------------- Search (PUBLIC) ----------------
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

        // ---------------- Search Result (FILTERED) ----------------
        composable(
            route = "resultSearch/{query}/{min}/{max}/{sort}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType },
                navArgument("min") { type = NavType.FloatType },
                navArgument("max") { type = NavType.FloatType },
                navArgument("sort") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val min = backStackEntry.arguments?.getFloat("min") ?: 0f
            val max = backStackEntry.arguments?.getFloat("max") ?: 1500f
            val sort = SortOption.valueOf(
                backStackEntry.arguments?.getString("sort") ?: SortOption.NEWEST.name
            )

            val viewModel: ResultSearchViewModel = viewModel()

            ResultSearchScreen(
                query = query,
//                initialFilter = SearchFilterState(min, max, sort),
                navController = navController,
                currentRoute = "search",
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------------- Product ----------------
        composable(
            route = "product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            ProductScreen(
                productId = it.arguments?.getInt("productId") ?: 0,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------------- Cart ----------------
        composable("cart") {
            CartScreen(navController = navController, currentRoute = "cart")
        }

        // ---------------- Checkout ----------------
        composable(
            route = "checkout?totalPrice={totalPrice}",
            arguments = listOf(navArgument("totalPrice") {
                type = NavType.StringType
                defaultValue = "0.0"
            })
        ) { backStackEntry ->
            val totalPrice =
                backStackEntry.arguments?.getString("totalPrice")?.toDoubleOrNull() ?: 0.0

            val viewModel: CheckoutViewModel = viewModel()

            LaunchedEffect(totalPrice) {
                viewModel.updateTotalPrice(totalPrice)
            }

            CheckoutScreen(navController = navController, viewModel = viewModel)
        }

        // ---------------- Address (LOGIN REQUIRED INSIDE SCREEN) ----------------
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
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<AddressUiModel>("address_to_edit")

                    navController.navigate("address_control")
                },
                onEditClick = { address ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("address_to_edit", address)
                    navController.navigate("address_control")
                },
                onAddressSelected = { address ->
                    addressViewModel.selectAddress(address.id)

                    if (from != "profile") {
                        val prev = navController.previousBackStackEntry?.destination?.route
                        if (prev?.contains("checkout") == true) {
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
            val addressToEdit =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<AddressUiModel>("address_to_edit")

            AddressControl(
                addressToEdit = addressToEdit,
                onBackClick = { navController.popBackStack() },
                onSaveClick = {
                    addressViewModel.addOrUpdateAddress(it)
                    navController.popBackStack()
                },
                onDeleteClick = {
                    addressViewModel.deleteAddress(it)
                    navController.popBackStack()
                }
            )
        }

        // ---------------- Checkout Payment ----------------
        composable(
            route = "checkout_payment?productPrice={productPrice}&shippingPrice={shippingPrice}&shippingLabel={shippingLabel}",
            arguments = listOf(
                navArgument("productPrice") { defaultValue = "0.0" },
                navArgument("shippingPrice") { defaultValue = "0.0" },
                navArgument("shippingLabel") { defaultValue = "Free shipping" }
            )
        ) { backStackEntry ->
            CheckoutPaymentScreen(
                navController = navController,
                productPrice = backStackEntry.arguments
                    ?.getString("productPrice")
                    ?.toDoubleOrNull() ?: 0.0,
                shippingPrice = backStackEntry.arguments
                    ?.getString("shippingPrice")
                    ?.toDoubleOrNull() ?: 0.0,
                shippingLabel = backStackEntry.arguments
                    ?.getString("shippingLabel") ?: "Free shipping"
            )
        }

        // ---------------- Checkout Completed ----------------
        composable("checkout_completed") {
            CheckoutCompletedScreen(navController = navController)
        }

        // ---------------- Orders (LOGIN REQUIRED INSIDE SCREEN) ----------------
        composable(
            route = "orders?status={status}",
            arguments = listOf(
                navArgument("status") {
                    type = NavType.StringType
                    defaultValue = ShopTab.ON_THE_WAY.name
                }
            )
        ) { backStackEntry ->
            val status = ShopTab.valueOf(
                backStackEntry.arguments?.getString("status")!!
            )

            OrderScreen(
                navController = navController,
                currentRoute = "orders",
                initialTab = status
            )
        }


        // ---------------- Order Info (LOGIN REQUIRED INSIDE SCREEN) ----------------
        composable(
            route = "orderInfo/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->

            val userId by session.userId.collectAsState(initial = null)
            val orderId = backStackEntry.arguments!!.getInt("orderId")

            if (userId == null) {
                // Show loading instead of crashing
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                OrderInfoScreen(
                    navController = navController,
                    orderId = orderId,
                    userId = userId!!
                )
            }
        }

        // ---------------- Review ----------------
        composable(
            "review/{orderId}/{userId}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType }
            )
        ) {
            ReviewScreen(
                orderId = it.arguments!!.getInt("orderId"),
                userId = it.arguments!!.getInt("userId"),
                onBack = { navController.popBackStack() }
            )
        }


        // ---------------- Profile ----------------
        composable("profile") {
            ProfileScreen(
                navController = navController,
                currentRoute = "profile",
                onNavigate = { route ->
                    if (route == "address") {
                        navController.navigate("address?from=profile")
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

//        ---------------- Profile Setting ----------------
        composable("profile_setting") {
            ProfileSetting(onBack = { navController.popBackStack() })
        }

        // -------------- Voucher --------------------------
        composable("voucher") {
            VoucherScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}
