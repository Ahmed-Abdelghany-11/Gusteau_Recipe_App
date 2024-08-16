package com.example.recipeapp.authentication.signUp.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.authentication.signUp.repo.SignUpRepoImp
import com.example.recipeapp.authentication.signUp.validation.UserDataValidation
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModel
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModelFactory
import com.example.recipeapp.data.sharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.local.model.UserData
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var confirmPass: String
    private lateinit var signUpBtn: MaterialButton
    private lateinit var signInText: TextView
    private lateinit var progressBar: ProgressBar
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
        progressBar= view.findViewById(R.id.wait_signup)
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
            ),
            requireContext()
        )
        signUpViewModel =
            ViewModelProvider(this, signUpViewModelFactory)[SignUpViewModel::class.java]

    }

    private fun handleValidation(user: UserData, validationErrors: MutableMap<String, String>) {
        if (validationErrors.isEmpty()) {
           addUser(user)
            findNavController().navigate(R.id.action_registerFragment_to_recipeActivity)
        } else {
            clearErrors()
            setErrors(validationErrors)
        }

    }

    private fun saveUserId(){
        val email= emailInput.text.toString()
        val password=passwordInput.text.toString()
        signUpViewModel.saveUserIdInSharedPref(email, password)
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


    private fun addUser(user: UserData){
        lifecycleScope.launch {
            showProgressBar()
            delay(2000)
            signUpViewModel.insertUser(user)
            authSharedPref.setLoggedIn(true)
            saveUserId()
            hideProgressBar()
        }
    }

    private fun showProgressBar(){
        signUpBtn.visibility= View.GONE
        progressBar.visibility=View.VISIBLE
    }

    private fun hideProgressBar(){
        progressBar.visibility=View.GONE
        signUpBtn.visibility= View.VISIBLE
    }


}