package com.grosianu.jobseeker.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.ActivityHomeBinding
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
    private lateinit var auth: FirebaseAuth

    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViews()

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        if (auth.currentUser == null) {
            val intent = Intent(this, StartupActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            println(auth.currentUser!!.email.toString())
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

        val bottomAppBar: BottomAppBar = findViewById(R.id.bottom_app_bar)
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
                    hideBottomNavBar(bottomAppBar)
                }
                R.id.createFragment -> {
                    hideBottomNavBar(bottomAppBar)
                }
                R.id.editPostFragment -> {
                    hideBottomNavBar(bottomAppBar)
                }
                R.id.editApplicationFragment -> {
                    hideBottomNavBar(bottomAppBar)
                }
                R.id.applicationFragment -> {
                    hideBottomNavBar(bottomAppBar)
                }
                R.id.pdfViewFragment -> {
                    hideBottomNavBar(bottomAppBar)
                }
                R.id.applySelectResumeFragment -> {
                    hideBottomNavBar(bottomAppBar)
                }
                R.id.applyWriteMessageFragment -> {
                    hideBottomNavBar(bottomAppBar)
                }
                else -> {
                    showBottomNavBar(bottomAppBar)
                }
            }
        }

        setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    private fun showBottomNavBar(bottomAppBar: BottomAppBar) {
        bottomAppBar.run {
            performShow()
            animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    bottomAppBar.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    private fun hideBottomNavBar(bottomAppBar: BottomAppBar) {
        bottomAppBar.run {
            performHide()
            animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    private fun navigateToHome() {
        val directions = HomeFragmentDirections.actionGlobalHomeFragment()
        findNavController(R.id.nav_host_fragment_home).navigate(directions)
    }

    private fun navigateToDiscover() {
        val directions = DiscoverFragmentDirections.actionGlobalDiscoverFragment()
        findNavController(R.id.nav_host_fragment_home).navigate(directions)
    }

    private fun navigateToNews() {
        val directions = NotificationsFragmentDirections.actionGlobalNotificationsFragment()
        findNavController(R.id.nav_host_fragment_home).navigate(directions)
    }

    private fun navigateToAccount() {
        val directions = AccountFragmentDirections.actionGlobalAccountFragment()
        findNavController(R.id.nav_host_fragment_home).navigate(directions)
    }
}