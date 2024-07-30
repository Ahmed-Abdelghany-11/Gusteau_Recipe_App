package com.example.recipeapp.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.SharedPreference.AuthSharedPref

class AuthActivity : AppCompatActivity() {
    private lateinit var authSharedPref: AuthSharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        authSharedPref= AuthSharedPref(this)
//        if(authSharedPref.isLoggedIn())
//            findNavController(R.id.nav_host_fragment).navigate(
//                R.id.recipeActivity
//            )
//
//        else
//            findNavController(R.id.nav_host_fragment).navigate(
//                R.id.splashFragment
//            )
    }
}