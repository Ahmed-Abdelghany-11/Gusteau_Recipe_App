package com.example.recipeapp.recipe.modeDialog.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.sharedPreference.SettingSharedPref
import com.example.recipeapp.recipe.modeDialog.repo.ModeRepo

class ModeViewModel(
    private val modeRepo: ModeRepo
) : ViewModel() {
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    fun setDarkMode(isDark: Boolean) {
        modeRepo.setTheme(isDark)
        _isDarkMode.value = isDark
    }

    fun initializeDarkMode() {
        val isDarkMode = modeRepo.getTheme()
        _isDarkMode.value = isDarkMode
    }
}