package com.example.android.mapviewer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.android.mapviewer.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

//class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var  actionBar: ActionBar
    // lateinit var viewModel:LogInViewModel

    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        //viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        val navController = this.findNavController(R.id.myNavHostFragment)
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

        //val toolbar = this.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        // NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration)


        // appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.welcomeFragment),
            drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView1, navController)
        //setUpToolbar()
       // val _view =  findViewById<NavigationView>(R.id.navView1)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    fun enableDisableDrawer(mode: Int) {
        if (drawerLayout != null) {
            drawerLayout.setDrawerLockMode(mode)
        }
    }

    private fun setUpToolbar() {

       // val mainActivity = mActivity as MainActivity
        val navigationView: NavigationView =  findViewById<NavigationView>(R.id.navView1)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView,navController)

       // val navController = NavHostFragment.findNavController(this)
       // val appBarConfiguration = mainActivity.appBarConfiguration
      //  NavigationUI.setupActionBarWithNavController(mainActivity,navController,appBarConfiguration)
       // NavigationUI.setupWithNavController(navigationView,navController)
        // val navigationView: NavigationView = mActivity?.findViewById(R.id.navView)//mainActivity.findViewById(R.id.navView)
       // val navController = NavHostFragment.findNavController(this)
        // val appBarConfiguration = mainActivity.appBarConfiguration
        // NavigationUI.setupActionBarWithNavController(mainActivity,navController,appBarConfiguration)
        // NavigationUI.setupWithNavController(navigationView,navController)

    }

     fun initializePortal()
     {

     }

}