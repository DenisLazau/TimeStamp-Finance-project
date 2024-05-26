package com.example.timestampfinance.ui.stocks

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

class StockViewModel : ViewModel() {

    private val _stockDetailsList = MutableLiveData<List<GlobalQuote>>()
    val stockDetailsList: LiveData<List<GlobalQuote>>
        get() = _stockDetailsList

    fun fetchMultipleStockDetails(symbols: List<String>) {
        GlobalScope.launch(Dispatchers.IO) {
            val stockDetails = mutableListOf<GlobalQuote>()
            for (symbol in symbols) {
                try {
                    val url = URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=$symbol&apikey=IME0OV7SE14RXJWR")
                    //URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
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
                    stockDetails.add(globalQuote)

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            _stockDetailsList.postValue(stockDetails)
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
