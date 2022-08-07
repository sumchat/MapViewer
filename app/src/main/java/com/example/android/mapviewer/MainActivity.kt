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

    /*
   *  Determines whether the app has the appropriate permissions across Android 10+ and all other
   *  Android versions.
   */
  //  @TargetApi(29)
 /*   private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }*/

    /*
     *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
     */
   // @TargetApi(29 )
   /* private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return

        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        val resultCode = when {
            runningQOrLater -> {
                // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }

        Log.d(TAG, "Request foreground only location permission")
        ActivityCompat.requestPermissions(
            this@MainActivity,
            permissionsArray,
            resultCode
        )
    }*/

    /*
    * In all cases, we need to have the location permission.  On Android 10+ (Q) we need to have
    * the background permission as well.
    */
  /*  override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED))
        {
            // Permission denied.
            Snackbar.make(
                binding.navView1,
                R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    // Displays App settings screen.
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
           // checkDeviceLocationSettingsAndStartGeofence()
        }
    }
*/

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