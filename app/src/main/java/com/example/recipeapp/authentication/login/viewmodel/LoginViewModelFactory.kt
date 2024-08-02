package com.example.recipeapp.authentication.login.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.authentication.login.repo.LoginRepository
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModel

class LoginViewModelFactory(val loginRepository: LoginRepository, val context: Context):ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            LoginViewModel(loginRepository,context) as T
        else throw IllegalArgumentException("LoginViewModel class not found")
    }
}