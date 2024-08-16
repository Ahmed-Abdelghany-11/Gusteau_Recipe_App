package com.example.recipeapp.authentication

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.sharedPreference.SettingSharedPref
import com.example.recipeapp.recipe.modeDialog.repo.ModeRepoImp
import com.example.recipeapp.recipe.modeDialog.viewModel.ModeViewModel
import com.example.recipeapp.recipe.modeDialog.viewModel.ModeViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var modeViewModel: ModeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        gettingViewModelReady()
        modeViewModel.initializeDarkMode()
        modeViewModel.isDarkMode.observe(this) { isDarkMode ->
            lifecycleScope.launch {
                applyTheme(isDarkMode)
            }
        }

        setContentView(R.layout.activity_main)
        hideSystemUI()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navToLogin = intent.getBooleanExtra("login", false)
        navController = findNavController(R.id.nav_host_fragment)
        navigateToLogin(navToLogin)

    }


    private fun navigateToLogin(navToLogin: Boolean) {
        val navOption = NavOptions.Builder()
            .setPopUpTo(R.id.splashFragment, true)
            .build()

        if (navToLogin) navController.navigate(R.id.loginFragment,null,navOption)
        else navController.navigate(R.id.splashFragment,null,navOption)

    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    private fun applyTheme(isDarkMode: Boolean) {
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun gettingViewModelReady() {
        val modeViewModelFactory = ModeViewModelFactory(
            ModeRepoImp(
                settingSharedPref = SettingSharedPref(this)
            )
        )
        modeViewModel = ViewModelProvider(this, modeViewModelFactory)[ModeViewModel::class.java]
    }



}