package com.grosianu.jobseeker.module.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.module.login.StartupActivity

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieving NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Action bar setup for use with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            val intent = Intent(this, StartupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Navigation handeling when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}