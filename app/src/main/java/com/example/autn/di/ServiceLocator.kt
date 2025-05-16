package com.example.autn.di

import android.content.Context
import com.example.autn.data.datasource.local.AuthLocalDataSource
import com.example.autn.data.datasource.local.AuthLocalDataSourceImpl
import com.example.autn.data.repository.AuthRepositoryImpl
import com.example.autn.domain.repository.AuthRepository
import com.example.autn.domain.usecase.IsUserLoggedInUseCase
import com.example.autn.domain.usecase.LoginUseCase
import com.example.autn.presentation.viewmodel.LoginViewModel

object ServiceLocator {

    private var authLocalDataSource: AuthLocalDataSource? = null
    private var authRepository: AuthRepository? = null

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

    fun provideLoginViewModel(context: Context): LoginViewModel {
        val repository = provideAuthRepository(context)
        val loginUseCase = LoginUseCase(repository)
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(repository)
        return LoginViewModel(loginUseCase, isUserLoggedInUseCase)
    }
}