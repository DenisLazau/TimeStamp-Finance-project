package com.example.timestampfinance.ui.stockdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.timestampfinance.databinding.FragmentStockDetailsBinding

class StockDetailsFragment : Fragment() {

    private lateinit var stockDetailsViewModel: StockDetailsViewModel
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

        val symbol = requireArguments().getString("symbol", "AAPL")

        stockDetailsViewModel = ViewModelProvider(this).get(StockDetailsViewModel::class.java)
        stockDetailsViewModel.fetchStockDetails(symbol)

        observeStockDetails()
    }

    private fun observeStockDetails() {
        stockDetailsViewModel.stockDetails.observe(viewLifecycleOwner, Observer { stockDetails ->
            displayStockDetails(stockDetails)
        })
    }

    private fun displayStockDetails(stockDetails: StockDetails) {
        binding.apply {
            textViewSymbol.text = "Symbol: ${stockDetails.globalQuote.symbol}"
            textViewPrice.text = "Price: ${stockDetails.globalQuote.price}"
            // Display other details similarly
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}