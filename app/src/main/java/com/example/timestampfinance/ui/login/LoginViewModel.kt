package com.example.timestampfinance.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToHome

    fun onLoginButtonClicked() {
        _navigateToHome.value = true
    }

    fun onLoginNavigationComplete() {
        _navigateToHome.value = false
    }
}