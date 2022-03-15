package com.grosianu.jobseeker.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.ui.home.HomeActivity

class StartupActivity : AppCompatActivity(R.layout.activity_startup) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hiding ActionBar
        supportActionBar?.hide()

        // Retrieving NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_startup) as NavHostFragment
        navController = navHostFragment.navController

        // Action bar setup for use with the NavController
        setupActionBarWithNavController(this, navController)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
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