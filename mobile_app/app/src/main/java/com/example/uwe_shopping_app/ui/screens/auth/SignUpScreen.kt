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
import com.example.uwe_shopping_app.util.isValidPhone
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

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
                text = "Create\nyour account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            UnderlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                placeholder = "Enter your name",
                isError = nameError != null,
                errorMessage = nameError
            )

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(16.dp))

            UnderlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    phoneError = null
                },
                placeholder = "Phone number",
                keyboardType = KeyboardType.Phone,
                isError = phoneError != null,
                errorMessage = phoneError
            )

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(16.dp))

            UnderlinedTextField(
                value = confirm,
                onValueChange = {
                    confirm = it
                    confirmError = null
                },
                placeholder = "Confirm password",
                isPassword = true,
                isError = confirmError != null,
                errorMessage = confirmError
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        nameError = null
                        emailError = null
                        phoneError = null
                        passwordError = null
                        confirmError = null

                        when {
                            name.isBlank() -> {
                                nameError = "Name is required"
                                return@launch
                            }

                            email.isBlank() -> {
                                emailError = "Email is required"
                                return@launch
                            }

                            !isValidEmail(email) -> {
                                emailError = "Invalid email format"
                                return@launch
                            }

                            phone.isBlank() -> {
                                phoneError = "Phone number is required"
                                return@launch
                            }

                            !isValidPhone(phone) -> {
                                phoneError = "Phone must be 9–11 digits"
                                return@launch
                            }

                            !isValidPassword(password) -> {
                                passwordError = "Password must be at least 8 characters"
                                return@launch
                            }

                            password != confirm -> {
                                confirmError = "Passwords do not match"
                                return@launch
                            }
                        }

                        isLoading = false

                        when (repo.registerUser(name, email, password, phone)) {
                            UserRepository.RegisterResult.Success -> {
                                val user = repo.loginUser(email, password)
                                if (user != null) {
                                    session.saveUserSession(
                                        user.id,
                                        user.email,
                                        user.phone
                                    )
                                    snackbarHostState.showSnackbar("Account created successfully")
                                    onSignUpSuccess()
                                }
                            }

                            UserRepository.RegisterResult.EmailExists -> {
                                emailError = "Email already exists"
                            }

                            UserRepository.RegisterResult.PhoneExists -> {
                                phoneError = "Phone number already exists"
                            }
                        }
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
                        "SIGN UP",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Row {
                Text("Already have an account? ")
                Text(
                    "Log In",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}
