package com.example.android.mapviewer

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.android.mapviewer.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

//class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var  actionBar: ActionBar
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController
    // lateinit var viewModel:LogInViewModel
    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
         navController = navHostFragment.navController

       // val navController = this.findNavController(R.id.myNavHostFragment)
        // val toolbar = this.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        actionBar = supportActionBar!!
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (actionBar != null) {
                if (destination.id == R.id.welcomeFragment) {
                    //  val actionBar: ActionBar? = supportActionBar

                    actionBar.hide()

                    // setSystemUIVisibility = View.Syste
                    // View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                } else {
                    actionBar.show()
                }
            }


            if (destination.id == R.id.mapFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                //DRAWER LOCKED IN fragment1


            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                //DRAWER UNLOCKED IN fragment2


            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.welcomeFragment),
            drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView1, navController)
        //setUpToolbar()
       // val _view =  findViewById<NavigationView>(R.id.navView1)
    }

    override fun onSupportNavigateUp(): Boolean {
        //val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    fun enableDisableDrawer(mode: Int) {
        if (drawerLayout != null) {
            drawerLayout.setDrawerLockMode(mode)
        }
    }

    private fun setUpToolbar() {

        val navigationView: NavigationView =  findViewById<NavigationView>(R.id.navView1)
       // val navController = this.findNavController(R.id.myNavHostFragment) -- deprecated
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView,navController)


    }


    fun initializePortal()
     {

     }

}

const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
private const val TAG = "MapViewerMainActivity"
const val LOCATION_PERMISSION_INDEX = 0
const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1