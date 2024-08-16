package com.example.recipeapp.recipe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.example.recipeapp.R
import com.example.recipeapp.authentication.AuthActivity
import com.example.recipeapp.recipe.common.CheckInternetViewModel
import com.example.recipeapp.recipe.common.OnSignOutClickListener
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.example.recipeapp.recipe.modeDialog.viewModel.ModeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeActivity : AppCompatActivity(), OnSignOutClickListener {
    private lateinit var navController: NavController
    private var isInitialLoad= true

    private val checkInternetViewModel: CheckInternetViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private val modeViewModel: ModeViewModel by viewModels()

    private lateinit var noInternet : TextView
    private lateinit var internetBack : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        modeViewModel.initializeDarkMode(this)
        modeViewModel.isDarkMode.observe(this) { isDarkMode ->
            applyTheme(isDarkMode)
        }

        setContentView(R.layout.activity_recipe)
        hideSystemUI()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        NavigationUI.setupWithNavController(bottomNav, navController)

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar, navController)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(
                        R.id.homeFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .build()
                    )
                    true
                }

                R.id.favouritesFragment -> {
                    navController.navigate(
                        R.id.favouritesFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .build()
                    )
                    true
                }

                R.id.searchFragment -> {
                    navController.navigate(
                        R.id.searchFragment, null, NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .build()
                    )
                    true
                }

                else -> false
            }
        }


        // Handle back button behavior
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack())
                    finishAffinity()
            }
        })

        noInternet = findViewById(R.id.noInternetTextView)
        internetBack = findViewById(R.id.InternetRestored)
        checkInternetViewModel.isOnline.observe(this){ isOnline ->
            if (isOnline) {
                if (!isInitialLoad) {
                   internetBack.visibility = View.VISIBLE
                    noInternet.visibility = View.GONE
                    Handler(Looper.getMainLooper()).postDelayed({
                        internetBack.visibility = View.GONE
                    }, 1000)
                }
            } else {
                noInternet.visibility = View.VISIBLE
                internetBack.visibility = View.GONE
            }
            isInitialLoad = false
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signOut -> {
                showSignOutDialog()
                true
            }

            R.id.mode -> {
                showModeDialog()
                true
            }

            else -> {
                item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showModeDialog() {
        navController.navigate(R.id.modeFragment)
    }

    private fun showSignOutDialog() {
      navController.navigate(R.id.signOutDialogFragment2)
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

    override fun signOut() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra("login", true)
        startActivity(intent)
        finishAffinity()

        // Clear login status
        AuthSharedPref(this).clearLoginStatus()

    }

    private fun applyTheme(isDarkMode: Boolean) {
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

}