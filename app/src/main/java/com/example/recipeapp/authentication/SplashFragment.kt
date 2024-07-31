package com.example.recipeapp.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref

class SplashFragment : Fragment() {

    private lateinit var authSharedPref: AuthSharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    authSharedPref=AuthSharedPref(requireContext())

        if (authSharedPref.isLoggedIn()) {
            findNavController().navigate(
                R.id.action_splashFragment_to_recipeActivity)

            requireActivity().finish()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(
                    R.id.action_splashFragment_to_loginFragment, null, NavOptions.Builder()
                        .setPopUpTo(R.id.splashFragment, inclusive = true)
                        .build()
                )


            }, 5000)
        }
    }



            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?,
            ): View? {

                return inflater.inflate(R.layout.fragment_splash, container, false)
            }

        }
