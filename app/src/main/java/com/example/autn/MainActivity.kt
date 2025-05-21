package com.example.autn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.autn.di.ServiceLocator
import com.example.autn.presentation.Navigation.AuthNavigation
import com.example.autn.ui.theme.AutnTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var isLoading = true
    private var startDestination = "login"

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition { isLoading }

        super.onCreate(savedInstanceState)

        val loginViewModel = ServiceLocator.provideLoginViewModel(applicationContext)

        lifecycleScope.launch {
            try {
                startDestination = if (loginViewModel.isUserLoggedIn()) "home" else "login"

                delay(1000)
            } catch (e: Exception) {
                startDestination = "login"
            } finally {
                isLoading = false
            }
        }

        enableEdgeToEdge()
        setContent {
            AutnTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var destination by remember { mutableStateOf("login") }

                    LaunchedEffect(isLoading) {
                        if (!isLoading) {
                            destination = startDestination
                        }
                    }

                    AuthNavigation(
                        applicationContext = applicationContext,
                        startDestination = destination
                    )
                }
            }
        }
    }
}