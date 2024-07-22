package com.swetha.aqisample.viewmodel


import AQIRepository
import AQIViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swetha.aqisample.utils.TokenManager

class AQIViewModelFactory(
    private val repository: AQIRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AQIViewModel::class.java)) {
            return AQIViewModel(repository, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
