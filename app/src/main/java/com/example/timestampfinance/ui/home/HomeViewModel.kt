package com.example.timestampfinance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _description = MutableLiveData<String>().apply {
        value = "Welcome to Finance News App! Stay updated with the latest finance news."
    }
    val description: LiveData<String> = _description

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun onLoginButtonClicked() {
        _navigateToLogin.value = true
    }

    fun onLoginNavigationComplete() {
        _navigateToLogin.value = false
    }
}