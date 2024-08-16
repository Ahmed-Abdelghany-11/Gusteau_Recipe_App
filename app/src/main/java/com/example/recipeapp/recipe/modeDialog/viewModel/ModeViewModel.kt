package com.example.recipeapp.recipe.modeDialog.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.recipe.modeDialog.repo.ModeRepo

class ModeViewModel(
    private val modeRepo: ModeRepo
) : ViewModel() {
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    fun setDarkMode(isDark: Boolean) {
        try {
            modeRepo.setTheme(isDark)
            _isDarkMode.value = isDark
        } catch (e: Exception) {
            Log.e("ModeViewModel", "Error setting dark mode: ${e.message}")
        }
    }

    fun initializeDarkMode() {
        try {
            val isDarkMode = modeRepo.getTheme()
            _isDarkMode.value = isDarkMode
        } catch (e: Exception) {
            Log.e("ModeViewModel", "Error initializing dark mode: ${e.message}")
        }
    }
}
