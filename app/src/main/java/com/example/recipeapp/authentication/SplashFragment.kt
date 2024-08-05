package com.example.recipeapp.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var authSharedPref: AuthSharedPref
    private lateinit var appNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authSharedPref = AuthSharedPref(requireContext())

        if (authSharedPref.isLoggedIn()) {
            if (isAdded) {
                findNavController().navigate(
                    R.id.action_splashFragment_to_recipeActivity
                )

                requireActivity().finish()
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded) {
                    findNavController().navigate(
                        R.id.action_splashFragment_to_loginFragment, null
                    )
                }
            }, 5000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appNameTextView = view.findViewById(R.id.app_name)
        appNameTextView.text = "Welcome To ${getString(R.string.app_name)}"
    }


}
