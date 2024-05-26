package com.example.timestampfinance.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.timestampfinance.R
import com.example.timestampfinance.ui.GlobalSettings

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Enable back button in the action bar
        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup topics spinner
        val topicsSpinner: Spinner = view.findViewById(R.id.topics_spinner)
        val topics = listOf("technology", "earnings")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, topics)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        topicsSpinner.adapter = adapter

        topicsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                GlobalSettings.selectedTopics = topics[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Setup stocks addition
        val stockEditText: EditText = view.findViewById(R.id.stock_edit_text)
        val addButton: Button = view.findViewById(R.id.add_stock_button)

        addButton.setOnClickListener {
            val stock = stockEditText.text.toString().trim()
            if (stock.isNotEmpty()) {
                GlobalSettings.symbols.add(stock)
                Toast.makeText(requireContext(), "Stock added: $stock", Toast.LENGTH_SHORT).show()
                stockEditText.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter a stock symbol", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
