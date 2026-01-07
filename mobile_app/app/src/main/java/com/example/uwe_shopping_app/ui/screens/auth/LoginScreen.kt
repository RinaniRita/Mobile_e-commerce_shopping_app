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
import com.example.uwe_shopping_app.ui.components.auth.UnderlinedTextField
import com.example.uwe_shopping_app.util.isValidEmail
import com.example.uwe_shopping_app.util.isValidPassword
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val repo = remember { UserRepository(session) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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

            UnderlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                placeholder = "Email address",
                keyboardType = KeyboardType.Email,
                isError = emailError != null,
                errorMessage = emailError
            )

            Spacer(Modifier.height(24.dp))

            UnderlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                placeholder = "Password",
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError
            )

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = {
                    scope.launch {
                        emailError = null
                        passwordError = null

                        when {
                            email.isBlank() -> {
                                emailError = "Email is required"
                                return@launch
                            }
                            !isValidEmail(email) -> {
                                emailError = "Invalid email format"
                                return@launch
                            }
                            password.isBlank() -> {
                                passwordError = "Password is required"
                                return@launch
                            }
                            !isValidPassword(password) -> {
                                passwordError = "Password must be at least 8 characters"
                                return@launch
                            }
                        }

                        isLoading = true

                        val user = repo.loginUser(email, password)

                        if (user != null) {
                            session.saveUserSession(
                                user.id,
                                user.email,
                                user.phone
                            )

                            snackbarHostState.showSnackbar("Login successful")
                            onLoginSuccess()
                        } else {
                            snackbarHostState.showSnackbar("Invalid email or password")
                        }

                        isLoading = false
                    }
                },
                modifier = Modifier
                    .width(180.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D201C)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        "LOG IN",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(48.dp))

            Row {
                Text("Don't have an account? ")
                Text(
                    "Sign Up",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}
