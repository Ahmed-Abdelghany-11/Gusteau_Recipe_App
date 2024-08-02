package com.example.recipeapp.authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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


        // Delay navigation to ensure NavController is ready
        lifecycleScope.launch {
            delay(1000)
            handleDeepLink(intent)
        }
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

}