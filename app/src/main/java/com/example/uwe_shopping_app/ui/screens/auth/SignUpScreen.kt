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
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
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
            text = "Create\nyour account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 60.dp)
        )

        UnderlinedTextField(name, { name = it }, "Enter your name")
        Spacer(Modifier.height(24.dp))
        UnderlinedTextField(email, { email = it }, "Email address", keyboardType = KeyboardType.Email)
        Spacer(Modifier.height(24.dp))
        UnderlinedTextField(password, { password = it }, "Password", isPassword = true)
        Spacer(Modifier.height(24.dp))
        UnderlinedTextField(confirm, { confirm = it }, "Confirm password", isPassword = true)

        Spacer(Modifier.height(40.dp))
        Button(
            onClick = {
                if (password != confirm) {
                    message = "Passwords do not match"
                    return@Button
                }

                CoroutineScope(Dispatchers.Main).launch {
                    val success = repo.registerUser(name, email, password)
                    if (success) {
                        // FIX: Thêm logic lưu email của người dùng
                        session.setUserEmail(email)
                        session.setLoggedIn(true)
                        onSignUpSuccess()
                    } else {
                        message = "Email already exists"
                    }
                }
            },
            modifier = Modifier.width(180.dp).height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D201C))
        ) {
            Text("SIGN UP", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (message.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(message, color = Color.Red)
        }

        Spacer(Modifier.height(40.dp))
        Text("or sign up with", fontSize = 14.sp, color = Color.Black.copy(alpha = 0.6f))
        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            SocialButton(R.drawable.apple)
            SocialButton(R.drawable.google)
            SocialButton(R.drawable.facebook)
        }

        Spacer(Modifier.height(48.dp))
        Row {
            Text("Already have account? ", fontSize = 14.sp)
            Text("Log In", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black, modifier = Modifier.clickable { onNavigateToLogin() })
        }
    }
}

//@Preview(showBackground = true, name = "Sign Up Screen")
//@Composable
//fun SignUpScreenPreview() {
//    Uwe_shopping_appTheme {
//        SignUpScreen()
//    }
//}