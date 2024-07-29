package com.example.recipeapp.home.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.home.home.repo.RetrofitRepoImp
import kotlinx.coroutines.launch

class HomeViewModel (private val myRepo : RetrofitRepoImp) : ViewModel() {
    private val _getMyResponse = MutableLiveData<MealList>()
    val getMyResponse: LiveData<MealList> = _getMyResponse

    fun getMyResponse() {
        viewModelScope.launch {
            _getMyResponse.postValue(myRepo.getMyResponse())
        }
    }
}