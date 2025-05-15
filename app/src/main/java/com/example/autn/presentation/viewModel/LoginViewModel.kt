package com.example.autn.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autn.domain.model.LoginRequest
import com.example.autn.domain.usecase.IsUserLoggedInUseCase
import com.example.autn.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
) : ViewModel() {

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            loginUseCase(loginRequest)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return isUserLoggedInUseCase()
    }
}