package com.example.autn.presentation.Navigation

import android.content.Context
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autn.di.ServiceLocator
import com.example.autn.presentation.ui.PortfolioScreen
import com.example.autn.presentation.ui.HomeScreen
import com.example.autn.presentation.ui.LoginScreen

@Composable
fun AuthNavigation(
    applicationContext: Context,
    startDestination: String = "login"
) {
    val navController = rememberNavController()

    val portfolioViewModel = remember {
        ServiceLocator.providePortfolioViewModel(applicationContext)
    }

    LaunchedEffect(startDestination) {
        if (startDestination == "home") {
            portfolioViewModel.loadPortfolio()
        }
    }

    LaunchedEffect(Unit) {
        portfolioViewModel.loadPortfolio()
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            val viewModel = ServiceLocator.provideLoginViewModel(applicationContext)
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable("home") {
            HomeScreen(navController = navController, viewModel = portfolioViewModel)
        }
        composable("portfolio") {
            PortfolioScreen(navController = navController, viewModel = portfolioViewModel)
        }
    }
}

