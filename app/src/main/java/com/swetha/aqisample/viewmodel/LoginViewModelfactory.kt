package com.swetha.aqisample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swetha.aqisample.utils.TokenManager

class LoginViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}