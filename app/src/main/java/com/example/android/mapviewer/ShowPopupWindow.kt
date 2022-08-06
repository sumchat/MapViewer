package com.example.android.mapviewer

import android.Manifest

import android.annotation.TargetApi
import android.app.Activity
import android.app.PendingIntent
import android.content.Context//.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*

import android.widget.*
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment

//import android.R.layout
import com.example.android.mapviewer.databinding.FragmentSaveReminderBinding
import com.example.android.mapviewer.geofence.GeofenceBroadcastReceiver
import com.example.android.mapviewer.geofence.GeofencingConstants
import com.example.android.mapviewer.locationreminders.ReminderDataItem
import com.example.android.mapviewer.locationreminders.SaveReminderViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

/*
public class ShowPopupWindow:Fragment() {
    lateinit var binding : FragmentSaveReminderBinding
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var reminderData: ReminderDataItem
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private lateinit var _context:Context


     val _viewModel: SaveReminderViewModel by inject()

  /*  private val _viewModel: SaveReminderViewModel by lazy {
        ViewModelProvider(this).get(SaveReminderViewModel::class.java)
    }*/
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getContext()?.let { checkDeviceLocationSettingsAndStartGeofence(it) }
        }
    }

    private val requestPermissionsResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    {
            isGranted ->
        if(isGranted){
            Log.i("DEBUG","permission granted")
             checkDeviceLocationSettingsAndStartGeofence(_context)
        }
        else{
            Log.i("DEBUG", "permission denied")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _context = requireContext()
        binding = FragmentSaveReminderBinding.inflate(inflater)//inflater.inflate(R.layout.fragment_save_reminder,null)
        geofencingClient = LocationServices.getGeofencingClient(requireContext())
        // _viewModel.latitude.postValue(lat)
        // _viewModel.longitude.postValue(long)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = _viewModel
        binding.saveReminder.setOnClickListener {
            val title = _viewModel.reminderTitle.value
            val description = _viewModel.reminderDescription.value
            // val location = _viewModel.reminderSelectedLocationStr.value
            val latitude = _viewModel.latitude.value
            val longitude = _viewModel.longitude.value
            reminderData = ReminderDataItem(title, description, latitude, longitude)
            if (!_viewModel.validateEnteredData(reminderData)) {
                return@setOnClickListener
            }
            checkPermissionsAndStartGeofencing(_context)
        }
        //Initialize the elements of our window, install the handler
        /* val test2: TextView = popupView.findViewById(R.id.titleText)
         test2.setText(R.string.textTitle)
         val buttonEdit: Button = popupView.findViewById(R.id.messageButton)
         buttonEdit.setOnClickListener(object : OnClickListener() {
             fun onClick(v: View?) {
                 //As an example, display the message
                 //Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT)
                 //    .show()
             }
         })*/
    }

   /* fun showPopupWindow(view: View) {


        //Create a View object yourself through inflater
        _context = view.getContext()
        val inflater = _context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FragmentSaveReminderBinding.inflate(inflater)//inflater.inflate(R.layout.fragment_save_reminder,null)
        geofencingClient = LocationServices.getGeofencingClient(view.getContext())
       // _viewModel.latitude.postValue(lat)
       // _viewModel.longitude.postValue(long)

        //Specify the length and width through constants
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        //Create a window with our parameters
        val popupWindow = PopupWindow(binding.root, width, height, focusable)

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        binding.viewModel = _viewModel


        binding.saveReminder.setOnClickListener {
            val title = _viewModel.reminderTitle.value
            val description = _viewModel.reminderDescription.value
           // val location = _viewModel.reminderSelectedLocationStr.value
            val latitude = _viewModel.latitude.value
            val longitude = _viewModel.longitude.value
            reminderData = ReminderDataItem(title, description, latitude, longitude)
            if (!_viewModel.validateEnteredData(reminderData)) {
                return@setOnClickListener
            }
            checkPermissionsAndStartGeofencing(_context)
        }

        //Initialize the elements of our window, install the handler
       /* val test2: TextView = popupView.findViewById(R.id.titleText)
        test2.setText(R.string.textTitle)
        val buttonEdit: Button = popupView.findViewById(R.id.messageButton)
        buttonEdit.setOnClickListener(object : OnClickListener() {
            fun onClick(v: View?) {

                //As an example, display the message
                //Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT)
                //    .show()
            }
        })*/


        //Handler for clicking on the inactive zone of the window
        binding.root.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                //Close the window when clicked
                popupWindow.dismiss()
                return true
            }
        })
    }
*/
    /**
     * Starts the permission check and Geofence process only if the Geofence associated with the
     * current hint isn't yet active.
     */
    private fun checkPermissionsAndStartGeofencing(context:Context) {

        if (foregroundAndBackgroundLocationPermissionApproved(context)) {
            checkDeviceLocationSettingsAndStartGeofence(context)
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }

    /*
   *  Determines whether the app has the appropriate permissions across Android 10+ and all other
   *  Android versions.
   */
    @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(context:Context): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }
    /*
    *  Uses the Location Client to check the current state of location settings, and gives the user
    *  the opportunity to turn on location services within our app.
    */

    private fun checkDeviceLocationSettingsAndStartGeofence(context:Context,resolve:Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_LOW_POWER//LocationRequest.PRIORITY_LOW_POWER
        }
        val locationSettingRequestsBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(context)
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(locationSettingRequestsBuilder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->

            if (exception is ResolvableApiException && resolve) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resultLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("DeviceLocation", "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    this.requireView(),
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeofence(context)
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                Log.d("Devicelocation", "Device location enabled")
                addGeofenceForReminder()
            }
        }

    }

    /*
     * Adds a Geofence for the current clue if needed, and removes any existing Geofence. This
     * method should be called after the user has granted the location permission.  If there are
     * no more geofences, we remove the geofence and let the viewmodel know that the ending hint
     * is now "active."
     */
    private fun addGeofenceForReminder() {

        if (this::reminderData.isInitialized) {
            val currentGeofenceData = reminderData

            // Build the Geofence Object
            val geofence = Geofence.Builder()
                // Set the request ID, string to identify the geofence.
                .setRequestId(currentGeofenceData.id)
                // Set the circular region of this geofence.
                .setCircularRegion(
                    currentGeofenceData.latitude!!,
                    currentGeofenceData.longitude!!,
                    GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                // Set the expiration duration of the geofence. This geofence gets
                // automatically removed after this period of time.
                //.setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()

            // Build the geofence request
            val geofencingRequest = GeofencingRequest.Builder()
                // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
                // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
                // is already inside that geofence.
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)

                // Add the geofences to be monitored by geofencing service.
                .addGeofence(geofence)
                .build()

            val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
            intent.action = GeofencingConstants.ACTION_GEOFENCE_EVENT

            val geofencePendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            geofencingClient.addGeofences(geofencingRequest,geofencePendingIntent)
                .addOnSuccessListener {
                    // Geofences added.
                    Toast.makeText(
                        requireContext(), R.string.geofences_added,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    _viewModel.validateAndSaveReminder(reminderData)
                    Log.e("Add Geofence", geofence.requestId)
                    // Tell the viewmodel that we've reached the end of the game and
                    // activated the last "geofence" --- by removing the Geofence.
                    //viewModel.geofenceActivated()
                }
                .addOnFailureListener {
                    // Failed to add geofences.
                    Toast.makeText(
                        requireContext(), R.string.geofences_not_added,
                        Toast.LENGTH_SHORT
                    ).show()
                    if ((it.message != null)) {
                        Log.w("Geofencing", it.message!!)
                    }
                }
            // }


        }
    }

    /*
     *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
     */
    @TargetApi(29 )
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved(_context))
            return

        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        /* var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

         val resultCode = when {
             runningQOrLater -> {
                 // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                 permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                 REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
             }
             else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
         }*/

        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Toast.makeText(_context,
                R.string.permission_denied_explanation,Toast.LENGTH_SHORT)
                .show()
        }
        else
        {
            requestPermissionsResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        Log.d("Permission", "Request foreground only location permission")

    }

    /*
    * In all cases, we need to have the location permission.  On Android 10+ (Q) we need to have
    * the background permission as well.
    */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("Request Permissions", "onRequestPermissionResult")

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED))
        {
            // Permission denied.
            Snackbar.make(
                this.requireView(),
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
            checkDeviceLocationSettingsAndStartGeofence(_context)
        }
    }



    /* override fun getViewModelStore(): ViewModelStore {

         TODO("Not yet implemented")
     }*/
}
*/



public class ShowPopupWindow : DialogFragment() {
    lateinit var binding: FragmentSaveReminderBinding
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var reminderData: ReminderDataItem
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private lateinit var _context: Context
    val _viewModel: SaveReminderViewModel by inject()
    /*  private val _viewModel: SaveReminderViewModel by lazy {
          ViewModelProvider(this).get(SaveReminderViewModel::class.java)
      }*/
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getContext()?.let { checkDeviceLocationSettingsAndStartGeofence(it) }
        }
    }
   /* private val requestPermissionsResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    {
            permissionsStatusMap ->
        if (!permissionsStatusMap.containsValue(false)) {
            // all permissions are accepted
            Log.i("DEBUG", "all permissions are accepted")
            checkDeviceLocationSettingsAndStartGeofence(_context)
        } else {
           Log.i("DEBUG", "all permissions are not accepted")
        }


    }*/
    private val requestPermissionsResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    {
            isGranted ->
        if(isGranted){
            Log.i("DEBUG","permission granted")
            checkDeviceLocationSettingsAndStartGeofence(_context)
        }
        else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                Toast.makeText(
                    _context,
                    R.string.permission_denied_explanation, Toast.LENGTH_SHORT
                )
                    .show()
            }
            Log.i("DEBUG", "permission denied")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _context = requireContext()
        binding = FragmentSaveReminderBinding.inflate(inflater)//inflater.inflate(R.layout.fragment_save_reminder,null)
        geofencingClient = LocationServices.getGeofencingClient(requireContext())
        // _viewModel.latitude.postValue(lat)
        // _viewModel.longitude.postValue(long)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = _viewModel
        binding.saveReminder.setOnClickListener {
            val title = _viewModel.reminderTitle.value
            val description = _viewModel.reminderDescription.value
            // val location = _viewModel.reminderSelectedLocationStr.value
            val latitude = _viewModel.latitude.value
            val longitude = _viewModel.longitude.value
            reminderData = ReminderDataItem(title, description, latitude, longitude)
            if (!_viewModel.validateEnteredData(reminderData)) {
                return@setOnClickListener
            }
            checkPermissionsAndStartGeofencing(_context)
        }
        //Initialize the elements of our window, install the handler
        /* val test2: TextView = popupView.findViewById(R.id.titleText)
         test2.setText(R.string.textTitle)
         val buttonEdit: Button = popupView.findViewById(R.id.messageButton)
         buttonEdit.setOnClickListener(object : OnClickListener() {
             fun onClick(v: View?) {
                 //As an example, display the message
                 //Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT)
                 //    .show()
             }
         })*/
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
    /**
     * Starts the permission check and Geofence process only if the Geofence associated with the
     * current hint isn't yet active.
     */
    private fun checkPermissionsAndStartGeofencing(context: Context) {
        if (foregroundAndBackgroundLocationPermissionApproved(context)) {
            checkDeviceLocationSettingsAndStartGeofence(context)
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }
    /*
   *  Determines whether the app has the appropriate permissions across Android 10+ and all other
   *  Android versions.
   */
  /*  @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(context: Context): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ))
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }*/
    /*
 *  Determines whether the app has the appropriate permissions across Android 10+ and all other
 *  Android versions.
 */
    @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(context: Context): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }
    /*
    *  Uses the Location Client to check the current state of location settings, and gives the user
    *  the opportunity to turn on location services within our app.
    */
    private fun checkDeviceLocationSettingsAndStartGeofence(context: Context, resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_LOW_POWER//LocationRequest.PRIORITY_LOW_POWER
        }
        val locationSettingRequestsBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(context)
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(locationSettingRequestsBuilder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resultLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("DeviceLocation", "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    this.requireView(),
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeofence(context)
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("Devicelocation", "Device location enabled")
                addGeofenceForReminder()
            }
        }
    }
    /*
     * Adds a Geofence for the current clue if needed, and removes any existing Geofence. This
     * method should be called after the user has granted the location permission.  If there are
     * no more geofences, we remove the geofence and let the viewmodel know that the ending hint
     * is now "active."
     */
    private fun addGeofenceForReminder() {
        if (this::reminderData.isInitialized) {
            val currentGeofenceData = reminderData
            // Build the Geofence Object
            val geofence = Geofence.Builder()
                // Set the request ID, string to identify the geofence.
                .setRequestId(currentGeofenceData.id)
                // Set the circular region of this geofence.
                .setCircularRegion(
                    currentGeofenceData.latitude!!,
                    currentGeofenceData.longitude!!,
                    GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                // Set the expiration duration of the geofence. This geofence gets
                // automatically removed after this period of time.
                //.setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()
            // Build the geofence request
            val geofencingRequest = GeofencingRequest.Builder()
                // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
                // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
                // is already inside that geofence.
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                // Add the geofences to be monitored by geofencing service.
                .addGeofence(geofence)
                .build()
            val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
            intent.action = GeofencingConstants.ACTION_GEOFENCE_EVENT
            val geofencePendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener {
                    // Geofences added.
                    Toast.makeText(
                        requireContext(), R.string.geofences_added,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    _viewModel.validateAndSaveReminder(reminderData)
                    Log.e("Add Geofence", geofence.requestId)
                    dismiss()
                    // Tell the viewmodel that we've reached the end of the game and
                    // activated the last "geofence" --- by removing the Geofence.
                    //viewModel.geofenceActivated()
                }
                .addOnFailureListener {
                    // Failed to add geofences.
                    Toast.makeText(
                        requireContext(), R.string.geofences_not_added,
                        Toast.LENGTH_SHORT
                    ).show()
                    if ((it.message != null)) {
                        Log.w("Geofencing", it.message!!)
                    }
                }
            // }
        }
    }
    /*
     *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
     */
    @TargetApi(29)
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved(_context))
            return
        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        /* var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
         val resultCode = when {
             runningQOrLater -> {
                 // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                 permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                 REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
             }
             else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
         }*/
        //if forgroundlocationpermission is approved check for backgroundlocation
       if( PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(_context,
                    Manifest.permission.ACCESS_FINE_LOCATION))
       {
           requestPermissionsResultLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

       }
        else
       {
           if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
               Toast.makeText(
                   _context,
                   R.string.permission_denied_explanation, Toast.LENGTH_SHORT
               )
                   .show()
           }
       }


    /*try {

        requestPermissionsResultLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )
    }
    catch(ex:Exception)
    {
        Log.d("Permission",ex.message.toString())
    }

            //requestPermissionsResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }*/
        Log.d("Permission", "Request foreground and background only location permission")

    }

    /*
 *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
 */
  /*  @TargetApi(29 )
    private fun requestForegroundAndBackgroundLocationPermissions(context:Context) {
        if (foregroundAndBackgroundLocationPermissionApproved(context))
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

        Log.d("Permission", "Request foreground only location permission")
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissionsArray,
            resultCode
        )
    }*/
    /*
    * In all cases, we need to have the location permission.  On Android 10+ (Q) we need to have
    * the background permission as well.
    */
   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("Request Permissions", "onRequestPermissionResult")
        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED)
        ) {
            // Permission denied.
            Snackbar.make(
                this.requireView(),
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
            checkDeviceLocationSettingsAndStartGeofence(_context)
        }
    }
    */
/* override fun getViewModelStore(): ViewModelStore {
         TODO("Not yet implemented")
     }*/
}