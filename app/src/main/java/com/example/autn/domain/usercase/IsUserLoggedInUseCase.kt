package com.example.autn.domain.usecase

import com.example.autn.domain.repository.AuthRepository

class IsUserLoggedInUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Boolean {
        return repository.isLoggedIn()
    }
}