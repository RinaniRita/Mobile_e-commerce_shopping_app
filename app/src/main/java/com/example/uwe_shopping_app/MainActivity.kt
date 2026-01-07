package com.example.uwe_shopping_app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.uwe_shopping_app.navigation.AppNavHost
import com.example.uwe_shopping_app.ui.theme.Uwe_shopping_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Uwe_shopping_appTheme {
                AppEntry(application)
            }
        }
    }
}

@Composable
fun AppEntry(app: Application) {
    val navController = rememberNavController()
    AppNavHost(navController, app)
}
