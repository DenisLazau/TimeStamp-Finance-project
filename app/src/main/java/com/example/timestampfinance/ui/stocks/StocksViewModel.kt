package com.example.timestampfinance.ui.stocks

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class NewsItem(
    val title: String,
    val url: String,
    val timePublished: String,
    val summary: String,
    val bannerImage: String,
    val source: String,
)

class StocksViewModel : ViewModel() {

    private val _newsData = MutableLiveData<List<NewsItem>>()
    val newsData: LiveData<List<NewsItem>> = _newsData

    fun updateNewsData(news: List<NewsItem>) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _newsData.value = news
            }
        }
    }
}
