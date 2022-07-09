package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityHomepageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Homepage : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        navController = (supportFragmentManager.findFragmentById(R.id.homeFragment) as NavHostFragment).navController

        setupWithNavController(binding.bottomNavBarHome, navController)

    }
}