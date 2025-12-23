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
import com.example.uwe_shopping_app.R // Đảm bảo import đúng R của project bạn
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme
import com.example.uwe_shopping_app.data.local.repository.UserRepository
import com.example.uwe_shopping_app.data.local.session.SessionManager
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
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val repo = remember { UserRepository() }
    val session = remember { SessionManager(context) }
    // Dùng rememberCoroutineScope thay vì tạo CoroutineScope mới
    val scope = rememberCoroutineScope()

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
                isLoading = true
                message = ""

                scope.launch {
                    // 1. Đăng ký
                    val registerSuccess = repo.registerUser(name, email, password)

                    if (registerSuccess) {
                        // 2. Nếu đăng ký thành công -> Gọi Login ngay để lấy User ID
                        val user = repo.loginUser(email, password)

                        if (user != null) {
                            // 3. Lưu ID và Email vào Session (QUAN TRỌNG)
                            session.saveUserSession(user.id, user.email)
                            isLoading = false
                            onSignUpSuccess()
                        } else {
                            isLoading = false
                            message = "Error logging in after signup"
                        }
                    } else {
                        isLoading = false
                        message = "Email already exists"
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
                Text("SIGN UP", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (message.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(message, color = Color.Red)
        }

        Spacer(Modifier.height(40.dp))

        Row {
            Text("Already have account? ", fontSize = 14.sp)
            Text("Log In", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black, modifier = Modifier.clickable { onNavigateToLogin() })
        }
    }
}