package com.example.recipeapp.authentication.login.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.authentication.login.repo.LoginRepoImp
import com.example.recipeapp.authentication.login.viewmodel.LoginViewModel
import com.example.recipeapp.authentication.login.viewmodel.LoginViewModelFactory
import com.example.recipeapp.authentication.signUp.repo.SignUpRepoImp
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModel
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModelFactory
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var signInBtn: MaterialButton
    private lateinit var signUpText: TextView
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var authSharedPref: AuthSharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailInput = view.findViewById(R.id.email)
        passwordInput = view.findViewById(R.id.password)
        authSharedPref = AuthSharedPref(requireContext())
        signInBtn = view.findViewById(R.id.signin_button)
        signUpText = view.findViewById(R.id.signIn_textView)


        //handle login
        getViewModelReady()
        // first check if email exists
        loginViewModel.isEmailExists.observe(viewLifecycleOwner) { emailExists ->
            if (emailExists) {
                loginViewModel.isUserExists.observe(viewLifecycleOwner) { userExists ->
                    if (userExists) {
                        passwordInput.error = null
                        authSharedPref.setLoggedIn(true)
                        saveUserId()
                        navigateToHome()
                    } else {
                        passwordInput.error = "incorrect password,please try again"
                    }

                }
            } else {
                showCreateAccountDialog()
            }
        }


        signInBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (checkInputs(email, password)) {
                loginViewModel.isEmailAlreadyExists(email)
                loginViewModel.isUserExists(email, password)
            } else
                setErrors(email, password)
        }

        signUpText.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun setErrors(email: String, password: String) {
        if (email.isBlank())
            emailInput.error = "Please enter your email"
        if (password.isBlank())
            passwordInput.error = "Please enter your password"
    }

    private fun checkInputs(email: String, password: String) =
        email.isNotBlank() && password.isNotBlank()


    // navigation
    private fun navigateToRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }


    private fun navigateToHome() =
        findNavController().navigate(R.id.action_loginFragment_to_recipeActivity)

    private fun getViewModelReady() {

        val loginViewModelFactory = LoginViewModelFactory(
            loginRepository = LoginRepoImp(
                localDataSource = LocalDataSourceImpl(requireContext())
            )
        )
        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]

    }

    private fun saveUserId(){
        val email= emailInput.text.toString()
        val password=passwordInput.text.toString()
         loginViewModel.getUserIdByEmailAndPassword(email, password)

        loginViewModel.userId.observe(viewLifecycleOwner){ id->
            authSharedPref.saveUserId(id)
        }
    }


    private fun showCreateAccountDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Email Not Found")
            .setMessage("This email does not exist. Would you like to create a new account?")
            .setPositiveButton("Create Account") { _, _ ->
                navigateToRegister()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}
