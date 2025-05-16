package com.example.autn.data.datasource.local

import android.content.Context
import com.example.autn.domain.model.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthLocalDataSourceImpl(context: Context) : AuthLocalDataSource {

    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override suspend fun saveUserCredentials(loginRequest: LoginRequest) {
        withContext(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putString("USER_EMAIL", loginRequest.email)
                putBoolean("REMEMBER_ME", loginRequest.rememberMe)

                if (loginRequest.rememberMe) {
                    putString("USER_PASSWORD", loginRequest.password)
                } else {
                    remove("USER_PASSWORD")
                }

                putBoolean("IS_LOGGED_IN", true)
                apply()
            }
        }
    }

    override suspend fun clearUserSession() {
        withContext(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                if (!sharedPreferences.getBoolean("REMEMBER_ME", false)) {
                    remove("USER_EMAIL")
                    remove("USER_PASSWORD")
                }
                putBoolean("IS_LOGGED_IN", false)
                apply()
            }
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }
}