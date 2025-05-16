package com.example.autn.presentation.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autn.R
import com.example.autn.di.ServiceLocator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthNavigation(applicationContext: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            val viewModel = ServiceLocator.provideLoginViewModel(applicationContext)
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable("home") {
            val viewModel = ServiceLocator.providePortfolioViewModel(applicationContext)
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("portfolio") {
            val viewModel = ServiceLocator.providePortfolioViewModel(applicationContext)
            PortfolioScreen(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun SplashScreen(navController: androidx.navigation.NavController) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        scope.launch {
            delay(2000) // 2 seconds delay
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}