package com.grosianu.jobseeker.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.ActivityHomeBinding
import com.grosianu.jobseeker.models.User
import com.grosianu.jobseeker.ui.home.destinations.account.AccountFragmentDirections
import com.grosianu.jobseeker.ui.home.destinations.discover.DiscoverFragmentDirections
import com.grosianu.jobseeker.ui.home.destinations.home.HomeFragmentDirections
import com.grosianu.jobseeker.ui.home.destinations.notifications.NotificationsFragmentDirections
import com.grosianu.jobseeker.ui.login.StartupActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var backPressedOnce = false
    private var isBottomNavVisible = true

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViews()
    }

    override fun onStart() {
        if (auth.currentUser == null) {
            val intent = Intent(this, StartupActivity::class.java)
            startActivity(intent)
            finish()
        }
        super.onStart()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_home)

        if (navController.graph.startDestinationId == navController.currentDestination?.id) {
            if (backPressedOnce) {
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setUpViews() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navController = navHostFragment.navController

        //val bottomAppBar: BottomAppBar = findViewById(R.id.bottom_app_bar)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        supportActionBar?.hide()

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.homeFragment,
                R.id.discoverFragment,
                R.id.resumeFragment,
                R.id.notificationsFragment,
                R.id.accountFragment
            )
        )

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            when (nd.id) {
                R.id.myPostsFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.myApplicationsFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.createFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.accountDetailsFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.accountAddDetailsFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.editPostFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.editApplicationFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.offerFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.applicationFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.applicantsFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.applicantFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.pdfViewFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.applySelectResumeFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.applyWriteMessageFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                R.id.applyCheckDetailsFragment -> {
                    hideBottomNav(bottomNavigationView, isBottomNavVisible)
                }
                else -> {
                    showBottomNav(bottomNavigationView, isBottomNavVisible)
                }
            }
        }

        setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    private fun showBottomNav(bottomNavigationView: BottomNavigationView, isVisible: Boolean) {
        if(isVisible) {
            return
        }

        ObjectAnimator.ofFloat(bottomNavigationView, "TranslationY", 0f).apply {
            duration = 150
            start()
            bottomNavigationView.visibility = View.VISIBLE
            isBottomNavVisible = true
        }
    }

    private fun hideBottomNav(bottomNavigationView: BottomNavigationView, isVisible: Boolean) {
        if (!isVisible) {
            return
        }

        val animator = ObjectAnimator.ofFloat(bottomNavigationView, "translationY", 200f)
        animator.apply {
            duration = 150
            start()
            doOnEnd { bottomNavigationView.isVisible = false }
        }
        isBottomNavVisible = false
    }
}