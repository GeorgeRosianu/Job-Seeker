package com.grosianu.jobseeker.module.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.module.home.HomeActivity


class StartupActivity : AppCompatActivity(R.layout.activity_startup) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieving NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Action bar setup for use with the NavController
        setupActionBarWithNavController(this, navController)

        auth = FirebaseAuth.getInstance()
    }

//    override fun onStart() {
//        super.onStart()
//
//        if (auth.currentUser != null) {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }

    /**
     * Navigation handeling when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}