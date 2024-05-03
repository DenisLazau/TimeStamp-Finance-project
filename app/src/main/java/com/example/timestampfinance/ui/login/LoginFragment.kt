package com.example.timestampfinance.ui.login

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

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe description LiveData and update UI
        loginViewModel.description.observe(viewLifecycleOwner, Observer {
            // Update UI with description
            view.findViewById<TextView>(R.id.descriptionTextView).text = it
        })

        // Navigate to login page when login button is clicked
        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            loginViewModel.onLoginButtonClicked()
        }

        // Observe navigateToLogin LiveData for navigation
        loginViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                loginViewModel.onLoginNavigationComplete()
            }
        })
    }
}