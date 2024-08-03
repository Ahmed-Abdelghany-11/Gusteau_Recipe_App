package com.example.recipeapp

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecipeActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe)
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
                    navController.navigate(R.id.homeFragment, null, NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build())
                    true
                }
                R.id.favouritesFragment -> {
                    navController.navigate(R.id.favouritesFragment, null, NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build())
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment, null, NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build())
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

//        val connectivityManager = getSystemService(ConnectivityManager::class.java)
//        //val currentNetwork = connectivityManager.activeNetwork
//        var firstTime = true
//        connectivityManager.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
//
//            override fun onAvailable(network: Network) {
//                if (!firstTime) {
//                    //Toast.makeText(applicationContext, "Internet is available", Toast.LENGTH_LONG)
//                        //.show()
//
//                }
//                //else firstTime = false
//            }
//            override fun onLost(network: Network) {
//                //Toast.makeText(applicationContext, "Internet is unavailable", Toast.LENGTH_LONG).show()
////                showDialog()
////                navController.navigate(R.id.action_global_to_noInternetFragment)
//            }

       // })


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

            else -> {
                item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showSignOutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Sign Out")
            .setMessage("Are You Sure That You Want To Sign Out")
            .setPositiveButton("Sure") { _, _ ->
                navigateToLogin()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://www.example.com/login")
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        // Clear login status
        AuthSharedPref(this).clearLoginStatus()

    }

}