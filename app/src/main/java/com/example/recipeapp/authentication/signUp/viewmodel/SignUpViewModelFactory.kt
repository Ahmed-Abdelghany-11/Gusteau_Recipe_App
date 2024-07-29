package com.example.recipeapp.authentication.signUp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.authentication.signUp.repo.SignUpRepository

class SignUpViewModelFactory(private val signUpRepository: SignUpRepository): ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignUpViewModel::class.java))
            SignUpViewModel(signUpRepository) as T
        else throw IllegalArgumentException("SignUpViewModel class not found")
    }
}