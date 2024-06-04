package com.example.timestampfinance.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData

class GlobalSettings {
    companion object {
        var selectedTopics: String = "technology"
        val symbols: MutableList<String> = mutableListOf("AAPL")
        val email = MutableLiveData<String>()

        private const val PREFS_NAME = "TimestampFinancePrefs"
        private const val KEY_SELECTED_TOPICS = "selectedTopics"
        private const val KEY_SYMBOLS = "symbols"
        private const val KEY_EMAIL = "email"

        init {
            // Set default values
            email.value = "Guest User"
        }

        fun loadSettings(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            selectedTopics = sharedPreferences.getString(KEY_SELECTED_TOPICS, "technology") ?: "technology"
            symbols.clear()
            symbols.addAll(sharedPreferences.getStringSet(KEY_SYMBOLS, setOf("AAPL")) ?: setOf("AAPL"))
            email.value = sharedPreferences.getString(KEY_EMAIL, "Guest User")
        }

        fun saveSettings(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString(KEY_SELECTED_TOPICS, selectedTopics)
                putStringSet(KEY_SYMBOLS, symbols.toSet())
                putString(KEY_EMAIL, email.value)
                apply()
            }
        }

        fun setEmail(context: Context, newEmail: String) {
            email.value = newEmail
            saveSettings(context)
        }
    }
}
