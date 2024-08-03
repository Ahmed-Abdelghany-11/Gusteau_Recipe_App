package com.example.recipeapp.authentication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.getSystemService
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
import java.security.AccessController.getContext

class AuthActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        navController = findNavController(R.id.nav_host_fragment)

        handleDeepLink(intent)

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        //val currentNetwork = connectivityManager.activeNetwork
        connectivityManager.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                Toast.makeText(applicationContext, "Internet is unavailable", Toast.LENGTH_LONG).show()
//                showDialog()
//                navController.navigate(R.id.action_global_to_noInternetFragment)
            }

        })


    }

    private fun handleDeepLink(intent: Intent) {
        val action = intent.action
        val data = intent.data
        if (Intent.ACTION_VIEW == action && data != null) {
            val destination = data.path
            if (destination == "/login") {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.auth_graph, true)
                    .build()
                navController.navigate(R.id.loginFragment, null, navOptions)
            }

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun showDialog() {
        runOnUiThread {
            if (!isFinishing && !isDestroyed) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Remove Meal From Favorites")
                    .setMessage("Are you sure you want to remove this meal from favorites?")
                    .setPositiveButton("Remove") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }


}