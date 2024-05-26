package com.example.timestampfinance.ui

import androidx.lifecycle.MutableLiveData

class GlobalSettings {
    companion object {
        var selectedTopics: String = "technology"
        val symbols: MutableList<String> = mutableListOf("AAPL")
        val email = MutableLiveData<String>()

        init {
            // Set default values
            email.value = "Guest User"
        }
    }
}