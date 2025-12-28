package com.example.uwe_shopping_app.data.chatbot

import com.example.uwe_shopping_app.data.local.repository.ProductRepository
import java.util.Locale

class LocalChatEngine(
    private val productRepository: ProductRepository,
    private val isLoggedIn: Boolean
) {
    suspend fun reply(input: String): String {
        val text = input.lowercase(Locale.getDefault())

        return when {
            isProductIntent(text) -> recommendProducts(text)
            isGuideIntent(text) -> guideUser(text)
            else -> defaultReply()
        }
    }

    private fun isProductIntent(text: String): Boolean =
        listOf("recommend", "suggest", "buy", "cheap", "price", "product")
            .any { it in text }

    private fun isGuideIntent(text: String): Boolean =
        listOf("how", "where", "add", "cart", "checkout", "order", "wishlist")
            .any { it in text }

    private fun normalize(text: String): String {
        return text
            .lowercase()
            .replace("ies", "y")   // accessories → accessory
            .replace("s ", " ")    // trailing plural
            .trim()
    }

    private val categoryKeywords = mapOf(
        "electronics" to listOf(
            "electronics", "electronic", "device", "devices", "gadget", "gadgets"
        ),
        "fashion" to listOf(
            "fashion", "cloth", "clothes", "clothing", "wear"
        ),
        "home" to listOf(
            "home", "house", "furniture", "living"
        ),
        "beauty" to listOf(
            "beauty", "makeup", "cosmetic", "cosmetics", "skincare"
        ),
        "sports" to listOf(
            "sports", "sport", "fitness", "gym", "exercise"
        )
    )
    private val expensiveKeywords = listOf("expensive", "premium", "luxury", "high end")
    private val ratingKeywords = listOf("best", "top rated", "rating", "stars")
    private val popularKeywords = listOf("popular", "trending", "hot", "bestseller")

    private suspend fun recommendProducts(text: String): String {
        val input = normalize(text)

        val products = when {
            // ---------- CATEGORY ----------
            categoryKeywords["electronics"]!!.any { it in input } ->
                productRepository.getProductsByCategory("Electronics", 0, 3)

            categoryKeywords["fashion"]!!.any { it in input } ->
                productRepository.getProductsByCategory("Fashion", 0, 3)

            categoryKeywords["home"]!!.any { it in input } ->
                productRepository.getProductsByCategory("Home", 0, 3)

            categoryKeywords["beauty"]!!.any { it in input } ->
                productRepository.getProductsByCategory("Beauty", 0, 3)

            categoryKeywords["sports"]!!.any { it in input } ->
                productRepository.getProductsByCategory("Sports", 0, 3)

            // ---------- PRICE ----------
            "cheap" in input || "low price" in input || "affordable" in input ->
                productRepository.getProductsSorted("price", "ASC", 0, 3)

            expensiveKeywords.any { it in input } ->
                productRepository.getProductsSorted("price", "DESC", 0, 3)

            // ---------- RATING ----------
            ratingKeywords.any { it in input } ->
                productRepository.searchProductsWithAvgRating(
                    query = "",
                    offset = 0,
                    limit = 3
                ).sortedByDescending { it.avgRating }
                    .map { it.product }

            // ---------- POPULAR ----------
            popularKeywords.any { it in input } ->
                productRepository.getProductsSorted("stock", "DESC", 0, 3)

            else ->
                productRepository.getProductsPaged(0, 3)
        }

        if (products.isEmpty()) {
            return "Sorry, I couldn't find suitable products."
        }

        return buildString {
            append("Here are some products you might like:\n\n")
            products.forEach {
                append("• ${it.name} - $${it.price}\n")
            }
        }
    }


    private fun guideUser(text: String): String {
        val input = normalize(text)

        // ---------- AUTH ----------
        if (!isLoggedIn) {
            return when {
                "login" in input || "sign in" in input ->
                    "Go to Profile → Login to access orders, cart, and checkout."

                "signup" in input || "register" in input ->
                    "Go to Profile → Sign up to create a new account."

                "cart" in input || "checkout" in input || "order" in input ->
                    "You need to log in first. Go to Profile → Login."

                else ->
                    "You can browse products freely. Log in to add to cart, checkout, or view orders."
            }
        }

        // ---------- LOGGED IN ----------
        return when {
            "add" in input && "cart" in input ->
                "Open a product → tap 'Add to Cart' → go to Cart."

            "checkout" in input ->
                "Go to Cart → press Checkout → select address and payment."

            "order" in input ->
                "Go to Profile → Orders to view your order status."

            "wishlist" in input ->
                "Tap the heart icon on a product to save it to your wishlist."

            "voucher" in input ->
                "Go to Profile → Vouchers to view available discounts."

            "payment" in input || "card" in input ->
                "Go to Profile → Payment to manage your cards."

            "address" in input ->
                "Go to Profile → Address to manage shipping addresses."

            "profile" in input ->
                "Go to Profile to manage your account, orders, and settings."

            else ->
                "You can ask me about products, cart, checkout, orders, wishlist, or vouchers."
        }
    }


    private fun defaultReply(): String =
        "I can help recommend products or guide you on how to use the app"
}
