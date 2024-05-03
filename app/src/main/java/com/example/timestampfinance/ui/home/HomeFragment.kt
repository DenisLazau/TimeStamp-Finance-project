package com.example.timestampfinance.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.timestampfinance.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe description LiveData and update UI
        homeViewModel.description.observe(viewLifecycleOwner, Observer {
            // Update UI with description
            view.findViewById<TextView>(R.id.descriptionTextView).text = it
        })

        // Navigate to login page when login button is clicked
        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            homeViewModel.onLoginButtonClicked()
        }

        // Observe navigateToLogin LiveData for navigation
        homeViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                homeViewModel.onLoginNavigationComplete()
            }
        })
    }
}