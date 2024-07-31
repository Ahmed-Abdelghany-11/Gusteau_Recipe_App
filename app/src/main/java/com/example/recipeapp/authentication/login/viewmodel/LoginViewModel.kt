package com.example.recipeapp.authentication.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.authentication.login.repo.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    // first check email
    private val _isEmailExists = MutableLiveData<Boolean>()
    val isEmailExists: LiveData<Boolean>
        get() = _isEmailExists


    private val _isUserExists = MutableLiveData<Boolean>()
    val isUserExists: LiveData<Boolean>
        get() = _isUserExists

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int>
        get() = _userId


    fun isUserExists(email: String, password: String) =
        viewModelScope.launch {
            _isUserExists.postValue(loginRepository.isUserExists(email, password))
        }


    fun isEmailAlreadyExists(email: String) =
        viewModelScope.launch {
            _isEmailExists.postValue(loginRepository.isEmailAlreadyExists(email))
        }

    fun getUserIdByEmailAndPassword(email: String, password: String) =
        viewModelScope.launch {
          _userId.postValue(loginRepository.getUserIdByEmailAndPassword(email, password))
        }


}