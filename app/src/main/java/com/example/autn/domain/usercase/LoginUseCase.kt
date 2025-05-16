package com.example.autn.domain.usecase

import com.example.autn.domain.model.LoginRequest
import com.example.autn.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest) {
        repository.login(loginRequest)
    }
}