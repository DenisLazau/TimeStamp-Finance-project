package com.example.timestampfinance.ui.news

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.timestampfinance.R
import com.example.timestampfinance.databinding.FragmentStocksBinding
import com.example.timestampfinance.ui.GlobalSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NewsFragment : Fragment() {

    private lateinit var stocksViewModel: NewsViewModel
    private var _binding: FragmentStocksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStocksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        fetchNewsSentiment()
        observeNewsData()
    }

    private fun setupViewModel() {
        stocksViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun fetchNewsSentiment() {
        if (!isInternetAvailable(requireContext())) {
            val selectedTopics = GlobalSettings.selectedTopics
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val url = URL("https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=AAPL&topics=$selectedTopics&limit=5&apikey=IME0OV7SE14RXJWR")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"

                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Save the response to a local file
                    saveResponseToFile(response)

                    val newsData = parseJsonResponse(response)
                    stocksViewModel.updateNewsData(newsData)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle error
                }
            }
        } else {
            fetchNewsSentimentFromLocalFile()
            Toast.makeText(requireContext(), "You are offline", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveResponseToFile(response: String) {
        val fileName = "news_sentiment.json"
        val file = File(requireContext().filesDir, fileName)
        FileOutputStream(file).use {
            it.write(response.toByteArray())
        }
    }

    private fun fetchNewsSentimentFromLocalFile() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Read data from local JSON file in internal storage
                val fileName = "news_sentiment.json"
                val file = File(requireContext().filesDir, fileName)
                if (file.exists()) {
                    val response = file.readText()
                    val newsData = parseJsonResponse(response)
                    stocksViewModel.updateNewsData(newsData)
                } else {
                    // Handle case where file does not exist
                    Toast.makeText(requireContext(), "No local data available", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle error
            }
        }
    }

    private fun parseJsonResponse(response: String): List<NewsItem> {
        val newsList = mutableListOf<NewsItem>()
        val jsonObject = JSONObject(response)
        val feedArray = jsonObject.getJSONArray("feed")

        for (i in 0 until feedArray.length()) {
            val newsObject = feedArray.getJSONObject(i)
            val newsItem = NewsItem(
                title = newsObject.getString("title"),
                url = newsObject.getString("url"),
                timePublished = newsObject.getString("time_published"),
                summary = newsObject.getString("summary"),
                bannerImage = newsObject.getString("banner_image"),
                source = newsObject.getString("source")
            )
            newsList.add(newsItem)
        }

        return newsList
    }

    private fun observeNewsData() {
        stocksViewModel.newsData.observe(viewLifecycleOwner, Observer { newsData ->
            displayNewsData(newsData)
        })
    }

    private fun displayNewsData(newsData: List<NewsItem>) {
        // Create an adapter with the news data
        val adapter = NewsAdapter(newsData)

        // Set the adapter to the RecyclerView
        binding.recyclerView.adapter = adapter
    }

    class NewsAdapter(private val newsData: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val newsItem = newsData[position]
            holder.bind(newsItem)
        }

        override fun getItemCount(): Int {
            return newsData.size
        }

        private suspend fun fetchImage(imageUrl: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL(imageUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val inputStream = connection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var titleView: TextView = itemView.findViewById(R.id.titleView)
            private val imageView: ImageView = itemView.findViewById(R.id.imageView)
            private val textView: TextView = itemView.findViewById(R.id.textView)

            fun bind(newsItem: NewsItem) {
                // Load image asynchronously
                GlobalScope.launch(Dispatchers.Main) {
                    val bitmap = fetchImage(newsItem.bannerImage)
                    imageView.setImageBitmap(bitmap)
                }

                // Display title
                titleView.text = newsItem.title

                // Display other information
                textView.text = """
                    Published: ${newsItem.timePublished}
                    Summary: ${newsItem.summary}
                    Source: ${newsItem.source}
                """.trimIndent()

                // Set click listener to open URL when the image is clicked
                imageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.url))
                    imageView.context.startActivity(intent)
                }
            }
        }
    }
}
