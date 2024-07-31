package com.example.recipeapp.authentication.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.authentication.login.repo.LoginRepository
import com.example.recipeapp.authentication.signUp.viewmodel.SignUpViewModel

class LoginViewModelFactory(val loginRepository: LoginRepository):ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            LoginViewModel(loginRepository) as T
        else throw IllegalArgumentException("LoginViewModel class not found")
    }
}