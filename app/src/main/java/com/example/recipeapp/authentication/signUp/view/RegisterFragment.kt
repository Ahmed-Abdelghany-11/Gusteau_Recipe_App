package com.example.recipeapp.authentication.signUp.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.authentication.signUp.repo.SignUpRepoImp
import com.example.recipeapp.authentication.signUp.validation.UserDataValidation
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModel
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModelFactory
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserData
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var confirmPass: String
    private lateinit var signUpBtn: MaterialButton
    private lateinit var signInText: TextView
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var authSharedPref: AuthSharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.findViewById(R.id.name)
        emailInput = view.findViewById(R.id.email)
        passwordInput = view.findViewById(R.id.password)
        confirmPasswordInput = view.findViewById(R.id.confirm_password)
        confirmPass = confirmPasswordInput.text.toString()
        signUpBtn = view.findViewById(R.id.signUp_button)
        signInText = view.findViewById(R.id.signIn_textView)
        authSharedPref=AuthSharedPref(requireContext())


        getViewModelReady()
        signUpViewModel.isEmailExists.observe(viewLifecycleOwner) { exists ->
            if (exists)
                emailInput.error = "This Email Already Exists"
            else {
                val user = UserData(
                    name = nameInput.text.toString(),
                    email = emailInput.text.toString(),
                    password = passwordInput.text.toString()
                )
                val validationErrors =
                    UserDataValidation.validateUserData(user, confirmPasswordInput.text.toString())
                handleValidation(user, validationErrors)
            }

        }

        signUpBtn.setOnClickListener {
            val email = emailInput.text.toString()
            signUpViewModel.isEmailAlreadyExists(email)

        }

        signInText.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun getViewModelReady() {

        val signUpViewModelFactory = SignUpViewModelFactory(
            signUpRepository = SignUpRepoImp(
                localDataSource = LocalDataSourceImpl(requireContext())
            )
        )
        signUpViewModel =
            ViewModelProvider(this, signUpViewModelFactory)[SignUpViewModel::class.java]

    }

    private fun handleValidation(user: UserData, validationErrors: MutableMap<String, String>) {
        if (validationErrors.isEmpty()) {
            signUpViewModel.insertUser(user)
            authSharedPref.setLoggedIn(true)
            saveUserId()
            findNavController().navigate(R.id.action_registerFragment_to_recipeActivity)
        } else {
            clearErrors()
            setErrors(validationErrors)
        }

    }

    private fun saveUserId(){
        val email= emailInput.text.toString()
        val password=passwordInput.text.toString()
        signUpViewModel.getUserIdByEmailAndPassword(email, password)

        signUpViewModel.userId.observe(viewLifecycleOwner){ id->
            authSharedPref.saveUserId(id)
        }
    }

    private fun clearErrors() {
        nameInput.error = null
        emailInput.error = null
        passwordInput.error = null
        confirmPasswordInput.error = null
    }

    private fun setErrors(validationErrors: MutableMap<String, String>) {
        validationErrors["name"]?.let {
            nameInput.error = it
        }

        validationErrors["email"]?.let {
            emailInput.error = it
        }

        validationErrors["password"]?.let {
            passwordInput.error = it
        }

        validationErrors["confirm-password"]?.let {
            confirmPasswordInput.error = it
        }
    }


}