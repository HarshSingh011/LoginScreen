package com.example.autn.domain.model

data class LoginRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean
)