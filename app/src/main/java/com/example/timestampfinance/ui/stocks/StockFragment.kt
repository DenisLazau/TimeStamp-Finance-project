package com.example.timestampfinance.ui.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timestampfinance.R
import com.example.timestampfinance.databinding.FragmentStockDetailsBinding
import com.example.timestampfinance.ui.GlobalSettings

class StockFragment : Fragment() {

    private lateinit var stockDetailsViewModel: StockViewModel
    private var _binding: FragmentStockDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stockDetailsViewModel = ViewModelProvider(this).get(StockViewModel::class.java)

        setupRecyclerView()
        stockDetailsViewModel.fetchMultipleStockDetails(GlobalSettings.symbols)
        observeStockDetails()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun observeStockDetails() {
        stockDetailsViewModel.stockDetailsList.observe(viewLifecycleOwner, Observer { stockDetailsList ->
            displayStockDetails(stockDetailsList)
        })
    }

    private fun displayStockDetails(stockDetailsList: List<GlobalQuote>) {
        val stockDetailsAdapter = StockDetailsAdapter(stockDetailsList)
        binding.recyclerView.adapter = stockDetailsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class StockDetailsAdapter(private var items: List<GlobalQuote>) : RecyclerView.Adapter<StockDetailsAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val symbolTextView: TextView = view.findViewById(R.id.Symbol)
            val priceTextView: TextView = view.findViewById(R.id.Price)
            val detailsTextView: TextView = view.findViewById(R.id.stockdetails)
            val changeArrow: ImageView = view.findViewById(R.id.changeArrow)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stock_detail, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.symbolTextView.text = "Symbol: ${item.symbol}"
            holder.priceTextView.text = "Current Price: ${item.price}"
            holder.detailsTextView.text = """
                Open: ${item.open}
                High: ${item.high}
                Low: ${item.low}
                Volume: ${item.volume}
                Latest Trading Day: ${item.latestTradingDay}
                Previous Close: ${item.previousClose}
                Change: ${item.change}
                Change Percent: ${item.changePercent}
            """.trimIndent()

            val changePercent = item.changePercent.removeSuffix("%").toFloatOrNull() ?: 0f
            if (changePercent > 0) {
                holder.changeArrow.setImageResource(R.drawable.up_trend)
            } else {
                holder.changeArrow.setImageResource(R.drawable.down_trend)
            }
            holder.changeArrow.visibility = View.VISIBLE
        }

        override fun getItemCount(): Int {
            return items.size
        }

        fun updateData(newItems: List<GlobalQuote>) {
            items = newItems
            notifyDataSetChanged()
        }
    }
}