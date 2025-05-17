// app/src/main/java/com/example/autn/di/ServiceLocator.kt
package com.example.autn.di

import android.content.Context
import com.example.autn.data.datasource.local.AuthLocalDataSource
import com.example.autn.data.datasource.local.AuthLocalDataSourceImpl
import com.example.autn.data.datasource.local.PortfolioLocalDataSource
import com.example.autn.data.datasource.local.PortfolioLocalDataSourceImpl
import com.example.autn.data.repository.AuthRepositoryImpl
import com.example.autn.data.repository.PortfolioRepositoryImpl
import com.example.autn.domain.repository.AuthRepository
import com.example.autn.domain.repository.PortfolioRepository
import com.example.autn.domain.usecase.*
import com.example.autn.presentation.viewmodel.LoginViewModel
import com.example.autn.presentation.viewmodel.PortfolioViewModel

object ServiceLocator {

    // Auth components
    private var authLocalDataSource: AuthLocalDataSource? = null
    private var authRepository: AuthRepository? = null

    // Portfolio components
    private var portfolioLocalDataSource: PortfolioLocalDataSource? = null
    private var portfolioRepository: PortfolioRepository? = null

    fun provideAuthLocalDataSource(context: Context): AuthLocalDataSource {
        return authLocalDataSource ?: synchronized(this) {
            AuthLocalDataSourceImpl(context).also {
                authLocalDataSource = it
            }
        }
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        return authRepository ?: synchronized(this) {
            val localDataSource = provideAuthLocalDataSource(context)
            AuthRepositoryImpl(localDataSource).also {
                authRepository = it
            }
        }
    }

    fun providePortfolioLocalDataSource(context: Context): PortfolioLocalDataSource {
        return portfolioLocalDataSource ?: synchronized(this) {
            PortfolioLocalDataSourceImpl(context).also {
                portfolioLocalDataSource = it
            }
        }
    }

    fun providePortfolioRepository(context: Context): PortfolioRepository {
        return portfolioRepository ?: synchronized(this) {
            val localDataSource = providePortfolioLocalDataSource(context)
            PortfolioRepositoryImpl(localDataSource).also {
                portfolioRepository = it
            }
        }
    }

    fun provideLoginViewModel(context: Context): LoginViewModel {
        val repository = provideAuthRepository(context)
        val loginUseCase = LoginUseCase(repository)
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(repository)
        return LoginViewModel(loginUseCase, isUserLoggedInUseCase)
    }

    fun providePortfolioViewModel(context: Context): PortfolioViewModel {
        val portfolioRepository = providePortfolioRepository(context)
        val authRepository = provideAuthRepository(context)

        val getPortfolioUseCase = GetPortfolioUseCase(portfolioRepository)
        val savePortfolioUseCase = SavePortfolioUseCase(portfolioRepository)
        val logoutUseCase = LogoutUseCase(authRepository)

        return PortfolioViewModel(getPortfolioUseCase, savePortfolioUseCase, logoutUseCase)
    }
}