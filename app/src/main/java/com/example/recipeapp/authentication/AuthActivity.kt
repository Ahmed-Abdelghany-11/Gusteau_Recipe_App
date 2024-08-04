package com.example.recipeapp.authentication

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
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


        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        //val currentNetwork = connectivityManager.activeNetwork
        var firstTime = true
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                if (!firstTime) {
                    Toast.makeText(applicationContext, "Internet is available", Toast.LENGTH_LONG)
                        .show()

                } else firstTime = false
            }

            override fun onLost(network: Network) {
                Toast.makeText(applicationContext, "Internet is unavailable", Toast.LENGTH_LONG)
                    .show()
//                showDialog()
//                navController.navigate(R.id.action_global_to_noInternetFragment)
            }

        })


    }

//    private fun showDialog() {
//        runOnUiThread {
//            if (!isFinishing && !isDestroyed) {
//                MaterialAlertDialogBuilder(this)
//                    .setTitle("Remove Meal From Favorites")
//                    .setMessage("Are you sure you want to remove this meal from favorites?")
//                    .setPositiveButton("Remove") { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    .setNegativeButton("Cancel") { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    .show()
//            }
//        }
//    }

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


}