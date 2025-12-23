package com.example.uwe_shopping_app.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uwe_shopping_app.data.local.repository.UserRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val repo = remember { UserRepository() }
    val session = remember { SessionManager(context) }
    // Use scope with composition is safer
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Log into\nyour account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        UnderlinedTextField(email, { email = it }, "Email address", keyboardType = KeyboardType.Email)
        Spacer(Modifier.height(24.dp))
        UnderlinedTextField(password, { password = it }, "Password", isPassword = true)

        Spacer(Modifier.height(16.dp))
        Text("Forgot Password?", fontSize = 14.sp, color = Color.Black.copy(alpha = 0.7f),
            modifier = Modifier.align(Alignment.End))

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    message = "Please fill in all fields"
                    return@Button
                }

                isLoading = true
                message = ""

                // LOGIN LOGIC
                coroutineScope.launch {

                    val user = repo.loginUser(email, password)

                    if (user != null) {
                        session.saveUserSession(user.id, user.email)
                        isLoading = false
                        onLoginSuccess()
                    } else {
                        isLoading = false
                        message = "Invalid email or password"
                    }
                }
            },
            modifier = Modifier.width(180.dp).height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D201C)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("LOG IN", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (message.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(message, color = Color.Red)
        }

        Spacer(Modifier.height(48.dp))
        Row {
            Text("Don't have an account? ", fontSize = 14.sp)
            Text(
                "Sign Up",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }
    }
}