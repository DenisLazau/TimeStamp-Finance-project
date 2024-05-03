package com.example.timestampfinance.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _description = MutableLiveData<String>().apply {
        value = "Welcome to Finance News App! Stay updated with the latest finance news."
    }
    val description: LiveData<String> = _description

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