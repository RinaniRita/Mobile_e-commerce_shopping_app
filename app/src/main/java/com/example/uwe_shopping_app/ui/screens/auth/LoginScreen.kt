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
import com.example.uwe_shopping_app.R
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import androidx.compose.ui.tooling.preview.Preview
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

    val context = LocalContext.current
    val repo = remember { UserRepository() }
    val session = remember { SessionManager(context) }

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

                // LOGIN LOGIC
                CoroutineScope(Dispatchers.Main).launch {
                    val success = repo.loginUser(email, password)
                    if (success) {
                        session.setLoggedIn(true)
                        onLoginSuccess()
                    } else {
                        message = "Invalid email or password"
                    }
                }
            },
            modifier = Modifier.width(180.dp).height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D201C))
        ) {
            Text("LOG IN", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (message.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(message, color = Color.Red)
        }

//        Spacer(Modifier.height(40.dp))
//        Text("or log in with", fontSize = 14.sp, color = Color.Black.copy(alpha = 0.6f))
//        Spacer(Modifier.height(20.dp))
//
//        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
//            SocialButton(R.drawable.apple)
//            SocialButton(R.drawable.google)      // tên đúng nếu bạn đổi tên file
//            SocialButton(R.drawable.facebook)
//        }

        Spacer(Modifier.height(48.dp))
        Row {
            Text("Don't have an account? ", fontSize = 14.sp)
            Text("Sign Up", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black, modifier = Modifier.clickable { onNavigateToSignUp() })
        }
    }
}

//@Preview(showBackground = true, name = "Login Screen")
//@Composable
//fun LoginScreenPreview() {
//    Uwe_shopping_appTheme {
//        LoginScreen()
//    }
//}