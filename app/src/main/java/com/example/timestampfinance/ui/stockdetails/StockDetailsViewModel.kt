package com.example.timestampfinance.ui.stockdetails


import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class StockDetailsViewModel : ViewModel() {

    private val _stockDetails = MutableLiveData<StockDetails>()
    val stockDetails: LiveData<StockDetails>
        get() = _stockDetails

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun fetchStockDetails(symbol: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url =
                    URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=$symbol&apikey=demo")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                try {
                    val jsonObject = JSONObject(response)
                    val globalQuoteObject = jsonObject.getJSONObject("Global Quote")

                    val globalQuote = GlobalQuote(
                        symbol = globalQuoteObject.getString("01. symbol"),
                        open = globalQuoteObject.getString("02. open"),
                        high = globalQuoteObject.getString("03. high"),
                        low = globalQuoteObject.getString("04. low"),
                        price = globalQuoteObject.getString("05. price"),
                        volume = globalQuoteObject.getString("06. volume"),
                        latestTradingDay = globalQuoteObject.getString("07. latest trading day"),
                        previousClose = globalQuoteObject.getString("08. previous close"),
                        change = globalQuoteObject.getString("09. change"),
                        changePercent = globalQuoteObject.getString("10. change percent")
                    )

                    val stockDetails = StockDetails(globalQuote)
                    _stockDetails.postValue(stockDetails)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON parsing error
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle other errors
            }
        }
    }
}

data class GlobalQuote(
    val symbol: String,
    val open: String,
    val high: String,
    val low: String,
    val price: String,
    val volume: String,
    val latestTradingDay: String,
    val previousClose: String,
    val change: String,
    val changePercent: String
)

data class StockDetails(
    val globalQuote: GlobalQuote
)