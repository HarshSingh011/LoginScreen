package com.example.autn.data.repository

import com.example.autn.data.datasource.local.AuthLocalDataSource
import com.example.autn.domain.model.LoginRequest
import com.example.autn.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    override suspend fun login(loginRequest: LoginRequest) {
        localDataSource.saveUserCredentials(loginRequest)
    }

    override suspend fun logout() {
        localDataSource.clearUserSession()
    }

    override fun isLoggedIn(): Boolean {
        return localDataSource.isUserLoggedIn()
    }
}