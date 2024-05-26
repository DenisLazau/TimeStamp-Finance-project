package com.example.timestampfinance.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _description = MutableLiveData<String>().apply {
        value = "TimeStamp Finance keeps you updated with the latest finance news, stock trends, and market insights, ensuring you stay informed."
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
