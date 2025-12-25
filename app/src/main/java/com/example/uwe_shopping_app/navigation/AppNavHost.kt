package com.example.uwe_shopping_app.navigation

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import com.example.uwe_shopping_app.ui.components.common.ShopTab
import com.example.uwe_shopping_app.ui.screens.address.*
import com.example.uwe_shopping_app.ui.screens.auth.LoginScreen
import com.example.uwe_shopping_app.ui.screens.auth.SignUpScreen
import com.example.uwe_shopping_app.ui.screens.cart.CartScreen
import com.example.uwe_shopping_app.ui.screens.checkout.*
import com.example.uwe_shopping_app.ui.screens.home.HomeScreen
import com.example.uwe_shopping_app.ui.screens.onboarding.WelcomeScreen
import com.example.uwe_shopping_app.ui.screens.order.OrderScreen
import com.example.uwe_shopping_app.ui.screens.orderInfo.OrderInfoScenario
import com.example.uwe_shopping_app.ui.screens.orderInfo.OrderInfoScreen
import com.example.uwe_shopping_app.ui.screens.product.ProductScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileScreen
import com.example.uwe_shopping_app.ui.screens.profile.ProfileSetting
import com.example.uwe_shopping_app.ui.screens.resultSearch.ResultSearchScreen
import com.example.uwe_shopping_app.ui.screens.resultSearch.ResultSearchViewModel
import com.example.uwe_shopping_app.ui.screens.search.SearchScreen
import com.example.uwe_shopping_app.ui.screens.search.SearchFilterState
import com.example.uwe_shopping_app.ui.screens.search.SortOption
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
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                    navController.navigate("login") {
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

        // ---------------- Home ----------------
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

        // ---------------- Search ----------------
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

        // Search result with filters
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
                initialFilter = SearchFilterState(min, max, sort),
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
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductScreen(productId = productId, onBack = { navController.popBackStack() })
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
            val totalPrice = backStackEntry.arguments
                ?.getString("totalPrice")
                ?.toDoubleOrNull() ?: 0.0

            val viewModel: CheckoutViewModel = viewModel()

            LaunchedEffect(totalPrice) {
                viewModel.updateTotalPrice(totalPrice)
            }

            CheckoutScreen(navController = navController, viewModel = viewModel)
        }

        // ---------------- Address ----------------
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

        // ---------------- Checkout result ----------------
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
                productPrice = backStackEntry.arguments?.getString("productPrice")?.toDoubleOrNull() ?: 0.0,
                shippingPrice = backStackEntry.arguments?.getString("shippingPrice")?.toDoubleOrNull() ?: 0.0,
                shippingLabel = backStackEntry.arguments?.getString("shippingLabel") ?: "Free shipping"
            )
        }

        composable("checkout_completed") {
            CheckoutCompletedScreen(navController = navController)
        }

        // ---------------- Orders  ----------------
        composable(
            route = "orders?status={status}",
            arguments = listOf(
                navArgument("status") {
                    type = NavType.StringType
                    defaultValue = ShopTab.ON_THE_WAY.name
                }
            )
        ) { backStack ->
            val status = ShopTab.valueOf(
                backStack.arguments?.getString("status")!!
            )

            OrderScreen(
                navController = navController,
                currentRoute = "orders",
                initialTab = status
            )
        }


        // ---------------- Orders Info ----------------
        composable(
            route = "orderInfo/{scenario}",
            arguments = listOf(navArgument("scenario") {
                type = NavType.StringType
            })
        ) { backStack ->
            val scenario = OrderInfoScenario.valueOf(
                backStack.arguments!!.getString("scenario")!!
            )

            OrderInfoScreen(
                navController = navController,
                scenario = scenario
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
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("home") { saveState = true }
                        }
                    }
                }
            )
        }

        composable("profile_setting") {
            ProfileSetting(onBack = { navController.popBackStack() })
        }
    }
}
