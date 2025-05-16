package com.example.autn.data.datasource.local

import com.example.autn.domain.model.LoginRequest

interface AuthLocalDataSource {
    suspend fun saveUserCredentials(loginRequest: LoginRequest)
    suspend fun clearUserSession()
    fun isUserLoggedIn(): Boolean
}