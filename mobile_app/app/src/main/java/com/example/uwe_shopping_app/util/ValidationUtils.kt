package com.example.uwe_shopping_app.util

import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8
}

fun isValidPhone(phone: String): Boolean {
    // digits only, 9–11 length
    return phone.matches(Regex("^[0-9]{9,11}$"))
}