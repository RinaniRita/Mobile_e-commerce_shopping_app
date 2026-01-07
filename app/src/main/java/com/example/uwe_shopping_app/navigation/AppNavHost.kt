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
import com.example.uwe_shopping_app.data.chatbot.ChatRepository
import com.example.uwe_shopping_app.data.local.database.AppDatabase
import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import com.example.uwe_shopping_app.ui.components.address.AddressUiModel
import com.example.uwe_shopping_app.ui.components.common.ShopTab
import com.example.uwe_shopping_app.ui.components.search.SortOption
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
import com.example.uwe_shopping_app.ui.screens.about.AboutUsScreen
import com.example.uwe_shopping_app.ui.screens.setting.SettingScreen
import com.example.uwe_shopping_app.ui.screens.setting.NotificationSettingScreen
import com.example.uwe_shopping_app.ui.screens.setting.TermsOfUseScreen
import com.example.uwe_shopping_app.ui.screens.setting.PrivacyPolicyScreen
import com.example.uwe_shopping_app.ui.screens.address.AddressScreen
import com.example.uwe_shopping_app.ui.screens.address.AddressViewModel
import com.example.uwe_shopping_app.ui.screens.voucher.VoucherScreen
import com.example.uwe_shopping_app.ui.screens.wishlist.WishlistScreen
import com.example.uwe_shopping_app.ui.screens.payment.PaymentScreen
import com.example.uwe_shopping_app.ui.screens.payment.AddNewCardScreen
import com.example.uwe_shopping_app.ui.components.payment.CardType
import com.example.uwe_shopping_app.ui.screens.auth.LoginScreen
import com.example.uwe_shopping_app.ui.screens.auth.SignUpScreen
import com.example.uwe_shopping_app.ui.screens.chat.ChatScreen
import com.example.uwe_shopping_app.ui.screens.chat.ChatViewModel
import com.example.uwe_shopping_app.ui.screens.checkout.CheckoutViewModel
import com.example.uwe_shopping_app.ui.screens.notification.NotificationScreen
import com.example.uwe_shopping_app.ui.screens.order.OrderScreen
import com.example.uwe_shopping_app.ui.screens.orderInfo.OrderInfoScreen
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

        // ---------------- Chat ----------------
        composable("chat") {
            val productRepo = ProductRepository()
            val chatRepo = ChatRepository(
                productRepository = productRepo,
                isLoggedIn = isLoggedIn == true
            )
            val chatViewModel = remember { ChatViewModel(chatRepo) }

            ChatScreen(
                viewModel = chatViewModel,
                onBack = { navController.popBackStack() }
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

        // ---------------- Search Result ----------------
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
            "checkout?totalPrice={totalPrice}",
            arguments = listOf(navArgument("totalPrice") {
                type = NavType.StringType
                defaultValue = "0.0"
            })
        ) { entry ->
            val totalPrice = entry.arguments?.getString("totalPrice")?.toDoubleOrNull() ?: 0.0
            val vm: CheckoutViewModel = viewModel()

            LaunchedEffect(totalPrice) {
                vm.updateTotalPrice(totalPrice)
            }

            CheckoutScreen(navController = navController, viewModel = vm)
        }

        // ---------------- Address ----------------
        composable(
            "address?from={from}&totalPrice={totalPrice}",
            arguments = listOf(
                navArgument("from") { defaultValue = "checkout" },
                navArgument("totalPrice") { defaultValue = "0.0" }
            )
        ) { entry ->
            val from = entry.arguments?.getString("from")
            val totalPrice = entry.arguments?.getString("totalPrice") ?: "0.0"
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
                onEditClick = {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("address_to_edit", it)
                    navController.navigate("address_control")
                },
                onAddressSelected = { address ->
                    addressViewModel.selectAddress(address.id)
                    if (from != "profile") {
                        navController.navigate("checkout?totalPrice=$totalPrice") {
                            popUpTo("cart")
                        }
                    }
                }
            )
        }

        composable("address_control") {
            val vm: AddressViewModel = viewModel()
            val addressToEdit =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<AddressUiModel>("address_to_edit")

            AddressControl(
                addressToEdit = addressToEdit,
                onBackClick = { navController.popBackStack() },
                onSaveClick = {
                    vm.addOrUpdateAddress(it)
                    navController.popBackStack()
                },
                onDeleteClick = {
                    vm.deleteAddress(it)
                    navController.popBackStack()
                }
            )
        }

        // ---------------- Checkout Payment ----------------
        composable(
            "checkout_payment?productPrice={productPrice}&shippingPrice={shippingPrice}&shippingLabel={shippingLabel}&address={address}&phone={phone}&discount={discount}",
            arguments = listOf(
                navArgument("productPrice") { defaultValue = "0.0" },
                navArgument("shippingPrice") { defaultValue = "0.0" },
                navArgument("shippingLabel") { defaultValue = "Free shipping" },
                navArgument("address") { defaultValue = "" },
                navArgument("phone") { defaultValue = "" },
                navArgument("discount") { defaultValue = "0.0" }
            )
        ) {
            CheckoutPaymentScreen(
                navController = navController,
                productPrice = it.arguments!!.getString("productPrice")!!.toDouble(),
                shippingPrice = it.arguments!!.getString("shippingPrice")!!.toDouble(),
                shippingLabel = it.arguments!!.getString("shippingLabel")!!,
                address = it.arguments!!.getString("address")!!,
                phone = it.arguments!!.getString("phone")!!,
                discount = it.arguments!!.getString("discount")!!.toDouble()
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


        // ---------------- Order Info  ----------------
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
                    when (route) {
                        "address" -> navController.navigate("address?from=profile")
                        "voucher" -> navController.navigate("voucher")
                        "wishlist" -> navController.navigate("wishlist")
                        "profile_setting" -> navController.navigate("profile_setting")
                        else -> navController.navigate(route)
                    }
                }
            )
        }

//        ---------------- Profile Setting ----------------
        composable("profile_setting") {
            ProfileSetting(onBack = { navController.popBackStack() })
        }

        // ---------------- About Us ----------------
        composable("about_us") {
            AboutUsScreen(onBack = { navController.popBackStack() })
        }

        // ---------------- Setting (App Settings) ----------------
        composable("setting") {
            SettingScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------------- Notification ----------------
        composable(
            route = "notification/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments!!.getInt("userId")

            NotificationScreen(
                userId = userId,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------------- Notification Settings ----------------
        composable("notification_setting") {
            NotificationSettingScreen(onBack = { navController.popBackStack() })
        }


        // ---------------- Terms of Use ----------------
        composable("terms_of_use") {
            TermsOfUseScreen(onBack = { navController.popBackStack() })
        }

        // ---------------- Privacy Policy ----------------
        composable("privacy_policy") {
            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
        }

         // -------------- Voucher --------------------------
         composable("voucher") {
             VoucherScreen(
                 onBackClick = { navController.popBackStack() }
             )
         }

         // -------------- Wishlist --------------------------
         composable("wishlist") {
             WishlistScreen(
                 navController = navController
             )
         }

         // -------------- Payment --------------------------
         composable("payment") {
             PaymentScreen(
                 navController = navController,
                 onBackClick = { navController.popBackStack() }
             )
         }

         // -------------- Add New Card --------------------------
         composable(
             route = "add_new_card?type={cardType}",
             arguments = listOf(
                 navArgument("cardType") {
                     type = NavType.StringType
                     defaultValue = ""
                 }
             )
         ) { backStackEntry ->
             val cardTypeString = backStackEntry.arguments?.getString("cardType") ?: ""
             val initialCardType = try {
                 if (cardTypeString.isNotEmpty()) {
                     CardType.valueOf(cardTypeString)
                 } else {
                     null
                 }
             } catch (e: Exception) {
                 null
             }

             AddNewCardScreen(
                 navController = navController,
                 initialCardType = initialCardType,
                 onBackClick = { navController.popBackStack() }
             )
         }

     }
 }
