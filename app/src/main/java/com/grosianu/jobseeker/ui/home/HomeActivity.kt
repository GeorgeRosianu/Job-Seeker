package com.grosianu.jobseeker.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.ui.login.StartupActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    //private lateinit var database: FirebaseFirestore

    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setting up Navigation Configuration
        setUpViews()

        auth = FirebaseAuth.getInstance()
        //database = FirebaseFirestore.getInstance()
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            val intent = Intent(this, StartupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

        var navController = findNavController(R.id.nav_host_fragment_home)

        if (navController.graph.startDestinationId == navController.currentDestination?.id) {

            if(backPressedOnce) {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                delay(2000)
                backPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Navigation handeling when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setUpViews() {
        // Retrieving NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navController = navHostFragment.navController

        // Bottom Navigation Setup
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        // Hiding ActionBar
        supportActionBar?.hide()

        var appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.homeFragment,
                R.id.discoverFragment,
                R.id.applicationsFragment,
                R.id.notificationsFragment,
                R.id.accountFragment
            )
        )

        // Action bar setup for use with the NavController
        setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}