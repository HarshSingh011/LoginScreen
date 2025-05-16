package com.example.autn.domain.repository

import com.example.autn.domain.model.LoginRequest

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest)
    suspend fun logout()
    fun isLoggedIn(): Boolean
}