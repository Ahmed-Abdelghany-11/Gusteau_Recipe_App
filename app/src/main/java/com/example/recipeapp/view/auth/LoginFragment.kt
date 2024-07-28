package com.example.recipeapp.view.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.google.android.material.button.MaterialButton


class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInBtn= view.findViewById<MaterialButton>(R.id.signin_button)
        signInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recipeActivity)
        }

    }
}