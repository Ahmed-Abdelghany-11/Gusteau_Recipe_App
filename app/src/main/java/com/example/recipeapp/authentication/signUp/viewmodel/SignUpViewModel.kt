package com.example.recipeapp.authentication.signUp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.authentication.signUp.repo.SignUpRepository
import com.example.recipeapp.data.sharedPreference.AuthSharedPref
import com.example.recipeapp.data.local.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpRepository: SignUpRepository,
    private val context: Context,
) : ViewModel() {

    private val _isEmailExists = MutableLiveData<Boolean>()
    val isEmailExists: LiveData<Boolean>
        get() = _isEmailExists

    fun insertUser(userData: UserData) = viewModelScope.launch {
        try {
            signUpRepository.insertUserData(userData)
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Error inserting user data: ${e.message}")
        }
    }

    fun isEmailAlreadyExists(email: String) = viewModelScope.launch {
        try {
            _isEmailExists.postValue(signUpRepository.isEmailAlreadyExists(email))
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Error checking if email exists: ${e.message}")
        }
    }

    fun saveUserIdInSharedPref(email: String, password: String) {
        viewModelScope.launch {
            try {
                val deferredId = async(Dispatchers.IO) {
                    signUpRepository.getUserIdByEmailAndPassword(email, password)
                }
                val userId = deferredId.await()
                AuthSharedPref(context).saveUserId(userId)
            } catch (e: Exception) {
                Log.e("SignUpViewModel", "Error saving user ID: ${e.message}")
            }
        }
    }
}
