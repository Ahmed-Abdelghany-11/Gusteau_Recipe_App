package com.example.recipeapp.authentication.login.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.authentication.login.repo.LoginRepository
import com.example.recipeapp.data.sharedPreference.AuthSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val context: Context,
) : ViewModel() {

    private val _isEmailExists = MutableLiveData<Boolean>()
    val isEmailExists: LiveData<Boolean>
        get() = _isEmailExists

    private val _isUserExists = MutableLiveData<Boolean>()
    val isUserExists: LiveData<Boolean>
        get() = _isUserExists

    fun isUserExists(email: String, password: String) = viewModelScope.launch {
        try {
            _isUserExists.postValue(loginRepository.isUserExists(email, password))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error checking if user exists: ${e.message}")
        }
    }

    fun isEmailAlreadyExists(email: String) = viewModelScope.launch {
        try {
            _isEmailExists.postValue(loginRepository.isEmailAlreadyExists(email))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error checking if email exists: ${e.message}")
        }
    }

    fun saveUserIdInSharedPref(email: String, password: String) {
        viewModelScope.launch {
            try {
                val deferredId = async(Dispatchers.IO) {
                    loginRepository.getUserIdByEmailAndPassword(email, password)
                }
                val userId = deferredId.await()
                AuthSharedPref(context).saveUserId(userId)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error saving user ID: ${e.message}")
            }
        }
    }
}
