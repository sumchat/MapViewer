package com.example.android.mapviewer

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.data.Feature
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.popup.Popup
import com.esri.arcgisruntime.mapping.popup.PopupField
import com.esri.arcgisruntime.portal.Portal
import com.esri.arcgisruntime.portal.PortalItem
//import com.example.android.mapviewer.databinding.FragmentMappageBinding
import com.example.android.mapviewer.data.FieldItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.esri.arcgisruntime.geometry.SpatialReferences

import com.esri.arcgisruntime.geometry.GeometryEngine
import com.esri.arcgisruntime.geometry.SpatialReferences.getWebMercator
import com.esri.arcgisruntime.mapping.view.*
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import com.example.android.mapviewer.locationreminders.ReminderDataItem
import com.example.android.mapviewer.locationreminders.SaveReminderViewModel
import com.example.android.mapviewer.reminderslist.RemindersListViewModel
import com.example.android.mapviewer.utils.fadeIn
import com.example.android.mapviewer.utils.fadeOut
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import com.esri.arcgisruntime.mapping.view.Graphic

import com.esri.arcgisruntime.geometry.Geometry
import com.esri.arcgisruntime.geometry.Point
import com.example.android.mapviewer.geofence.GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
import com.esri.arcgisruntime.symbology.SimpleFillSymbol

import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.example.android.mapviewer.locationreminders.ReminderObject
import com.example.android.mapviewer.locationreminders.data.ReminderDataSource


class MapFragment : Fragment(){
    lateinit var thiscontext: Context
    lateinit var _inflater:LayoutInflater
    private var parentLinearLayout: ConstraintLayout? = null
    private lateinit var viewOfLayout: View
    private lateinit var viewOfLayoutresults: View
    private lateinit var mRecyclerView1: RecyclerView
    private lateinit var headerText: TextView
    private lateinit var tabView:View
    private lateinit var viewPager: ViewPager2 // creating object of ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var behavior:BottomSheetBehavior<ConstraintLayout>
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private lateinit  var portalItem:PortalItem
    private var lat:Double = 0.0
    private var long:Double = 0.0
    public var _graphicsOverlay: GraphicsOverlay? = null
    private val selectedFeatures by lazy { ArrayList<Feature>() }
   // private val popupViewModel: PopupViewModel by viewModels()
   private lateinit var _fieldItemAdapter:FieldValueAdapter
    val _remindersviewModel: RemindersListViewModel  by inject()
    val remindersLocalRepository by inject<ReminderDataSource>()


    val _viewModel: SaveReminderViewModel by inject()

    public val mapView: MapView by lazy {
        viewOfLayoutresults.findViewById(R.id.mapView)
        //activityMainBinding.mapView
    }
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getContext()?.let { checkDeviceLocationSettingsAndStartPopup(it) }
        }
    }


   private val requestPermissionsResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
   {
           isGranted ->
       if(isGranted){
           Log.i("DEBUG","permission granted")
           checkDeviceLocationSettingsAndStartPopup(thiscontext)
       }
       else{
           Log.i("DEBUG", "permission denied")
       }
   }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun adapterOnClick(fieldItem: FieldItem) {
        //itemId  = portalItem.itemId
        //val action = MapGalleryFragmentDirections.actionMapGalleryFragmentToMapFragment(itemId)
        //findNavController().navigate(action)

          }
    fun deleteView(view: View) {
        val parentLinearLayout = viewOfLayoutresults.findViewById<ConstraintLayout>(R.id.sheet_container)

        parentLinearLayout!!.removeView(view.parent as View)
    }
    fun addView(childview: View) {

        val parentLinearLayout = viewOfLayoutresults.findViewById<ConstraintLayout>(R.id.sheet_container)


        parentLinearLayout!!.addView(childview, parentLinearLayout!!.childCount - 1)
        val identifyLayout = parentLinearLayout.findViewById<ConstraintLayout>(R.id.identify_container)
        val params = identifyLayout.layoutParams
        val _ht = parentLinearLayout.height
        //bottomSheet.getLayoutParams().height = bottomSheet.getHeight() - accountHeight;
       // parentLinearLayout.requestLayout();
        params.height = _ht

        identifyLayout.setLayoutParams(params)




        tabLayout = parentLinearLayout.findViewById<TabLayout>(R.id.tab_layout)
        viewPager = parentLinearLayout.findViewById<ViewPager2>(R.id.pager)


        val saveReminderBtn = parentLinearLayout.findViewById<FloatingActionButton>(R.id.saveReminder)
        saveReminderBtn.setOnClickListener{
            checkPermissionsAndOpenPopup(thiscontext)

        }
    }

    fun addIdentifyView()
    {
        val bottomsheet = viewOfLayoutresults.findViewById<ConstraintLayout>(R.id.sheet_container)
        // get ahold of the instance of your layout
        // get ahold of the instance of your layout
        //val dynamicContent = findViewById(R.id.dynamic_content) as LinearLayout

        behavior = BottomSheetBehavior.from(bottomsheet)
        // behavior.setPeekHeight(200)

      //  val identifyView: View = _inflater.inflate(R.layout.fragment_identify, null)
        val identifyLayout = bottomsheet.findViewById<ConstraintLayout>(R.id.identify_container)
        if(identifyLayout === null) {
            val identifyView: View =
                LayoutInflater.from(thiscontext).inflate(R.layout.fragment_identify, null)


            addView(identifyView)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _inflater = inflater
        if (container != null) {
            thiscontext = container.getContext()
        }
        viewOfLayoutresults = inflater.inflate(R.layout.fragment_mappage, container, false)

        val _bottomSheet = viewOfLayoutresults.findViewById<ConstraintLayout>(R.id.sheet_container)
         behavior = BottomSheetBehavior.from(_bottomSheet)
        // behavior.setPeekHeight(200)



        tabView = viewOfLayoutresults.findViewById<View>(R.id.identify)//.visibility = View.VISIBLE

        tabLayout = viewOfLayoutresults.findViewById<TabLayout>(R.id.tab_layout)
        viewPager = viewOfLayoutresults.findViewById<ViewPager2>(R.id.pager)


         val saveReminderBtn = viewOfLayoutresults.findViewById<FloatingActionButton>(R.id.saveReminder)
       // saveReminderBtn.fadeIn()
         saveReminderBtn.setOnClickListener{
             checkPermissionsAndOpenPopup(thiscontext)

        }
        _remindersviewModel.remindersList.observe(viewLifecycleOwner, {
            it?.let {
                showReminders(it as MutableList<ReminderDataItem>)
            }
        })




        var args = MapFragmentArgs.fromBundle(requireArguments())
       // val _itemId = getArguments()?.getString("itemId")
        val _itemId: String = args.myArg
        val _title:String = args.mapTitle
        //portalItem = args.itemId
        setApiKeyForApp()
        setHasOptionsMenu(true)

        setupMap(_itemId,_title)
        _graphicsOverlay = GraphicsOverlay()
        val pointSymbol =
            SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10f)
        val pointRenderer = SimpleRenderer(pointSymbol)
        _graphicsOverlay?.renderer = pointRenderer
        mapView.graphicsOverlays.add(_graphicsOverlay)
        //viewOfLayout = inflater.inflate(R.layout.fieldvalue_row, container, false)
        return viewOfLayoutresults // activityMainBinding.root

    }

    fun showReminders(remindersList: MutableList<ReminderDataItem>)
    {
        renderPointGraphicsOverlay(remindersList)
    }

    /**
     * Create a point, its graphic, a graphics overlay for it, and add it to the map view.
     * */
    private fun renderPointGraphicsOverlay(remindersList: MutableList<ReminderDataItem>) {
        mapView.graphicsOverlays.clear()
        val _graphicsOverlay = GraphicsOverlay()
        val _planarGraphicsOverlay = GraphicsOverlay()
        // create a fill symbol for planar buffer polygons
        // create a fill symbol for planar buffer polygons
        val planarOutlineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2F)
        val planarBufferFillSymbol = SimpleFillSymbol(
            SimpleFillSymbol.Style.SOLID, Color.RED,
            planarOutlineSymbol
        )
        val circleRenderer = SimpleRenderer(planarBufferFillSymbol)
        _planarGraphicsOverlay.renderer = circleRenderer
        _planarGraphicsOverlay.opacity= 0.5f
        val wgs84 = SpatialReferences.getWgs84()
        val pointSymbol =
            SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10f)
        // create simple renderer
        val pointRenderer = SimpleRenderer(pointSymbol)
        _graphicsOverlay?.renderer = pointRenderer
        for (pt in remindersList) {
            val pointGeometry = pt.latitude?.let {
                pt.longitude?.let { it1 ->
                    com.esri.arcgisruntime.geometry.Point(
                        it1,
                        it,wgs84
                    )
                }
            }
            val pointGraphic = Graphic(pointGeometry)
            _graphicsOverlay?.graphics?.add(pointGraphic)
            // create a planar buffer graphic around the input location at the specified distance
            // create a planar buffer graphic around the input location at the specified distance
            val _projGeom = GeometryEngine.project(pointGeometry, getWebMercator())
            val bufferGeometryPlanar: Geometry = GeometryEngine.buffer(_projGeom,
                GEOFENCE_RADIUS_IN_METERS.toDouble()
            )
            val planarBufferGraphic = Graphic(bufferGeometryPlanar)
            _planarGraphicsOverlay.graphics.add(planarBufferGraphic)

        }
        mapView.graphicsOverlays.add(_graphicsOverlay)
        mapView.graphicsOverlays.add(_planarGraphicsOverlay)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //viewModel.logOut()
        //return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
         //       || super.onOptionsItemSelected(item)
       when(item.title){
          "Reminders" ->{

                  tabLayout.removeAllTabs()
                  val saveReminderBtn =
                      viewOfLayoutresults.findViewById<FloatingActionButton>(R.id.saveReminder)
                  saveReminderBtn.fadeOut()
                  tabLayout!!.addTab(tabLayout!!.newTab().setText("Reminders"))

                  tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
                  val identifyAdapter = IdentifyFragmentAdapter(
                      "ShowReminders",
                      null,
                      null,
                      requireActivity(),
                      tabLayout!!.tabCount
                  )//ViewPagerAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
                  viewPager!!.adapter = identifyAdapter

                  tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                      override fun onTabSelected(tab: TabLayout.Tab) {
                          viewPager!!.currentItem = tab.position
                      }

                      override fun onTabUnselected(tab: TabLayout.Tab) {

                      }

                      override fun onTabReselected(tab: TabLayout.Tab) {

                      }
                  })
                  (view?.height)?.div(2)?.let { behavior.setPeekHeight(it) }
                  behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED//STATE_EXPANDED

                  _remindersviewModel.remindersList.observe(viewLifecycleOwner, {
                      it?.let {
                          showReminders(it as MutableList<ReminderDataItem>)
                      }
                  })

                  _remindersviewModel.activeReminderDataItem.observe(viewLifecycleOwner, {
                      it?.let {
                          showOrDeleteReminderLocation(it as ReminderObject)
                      }
                  })
                  return true


          }
           else -> return super.onOptionsItemSelected(item);
       }


    }

    fun showOrDeleteReminderLocation(reminderDataObject: ReminderObject)
    {
        val reminderDataItem = reminderDataObject.reminderItem
        if(reminderDataObject.action === "zoom") {
            val wgs84 = SpatialReferences.getWgs84()
            //for (pt in remindersList) {
            val pointGeometry = reminderDataItem?.latitude?.let {
                reminderDataItem.longitude?.let { it1 ->
                    com.esri.arcgisruntime.geometry.Point(
                        it1,
                        it, wgs84
                    )
                }
            }
            val _projGeom = GeometryEngine.project(pointGeometry, getWebMercator())
            // set viewpoint of map view to starting point and scale
            mapView.setViewpointCenterAsync(_projGeom as Point?, mapView.mapScale)
        }
        else
        {
           val geofencingClient = LocationServices.getGeofencingClient(thiscontext)
            val geofenceId = reminderDataItem?.id
            geofencingClient.removeGeofences(listOf(geofenceId))?.run {
                addOnSuccessListener { //in case of success removing
                    Log.d("GeofenceUtil", getString(R.string.geofence_removed))
                    Toast.makeText(
                        thiscontext,
                        getString(R.string.geofence_removed),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (geofenceId != null) {
                        try {
                            _remindersviewModel.deleteReminder(geofenceId)

                        }
                        catch(ex:java.lang.Exception)
                        {
                            val error = "Error deleting reminder: " + ex.message
                        }
                       // remindersLocalRepository.deleteReminder(geofenceId)
                       //_viewModel.deleteReminder(geofenceId)
                    }

                }
                addOnFailureListener { ////in case of failure
                    Toast.makeText(
                        thiscontext,
                        getString(R.string.geofence_not_removed),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.d("GeofenceUtil", getString(R.string.geofence_not_removed))
                }
            }

        }

    }



    /*
 *  Determines whether the app has the appropriate permissions across Android 10+ and all other
 *  Android versions.
 */
    @TargetApi(29)

        private fun foregroundLocationPermissionApproved(context: Context): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION))

        return foregroundLocationApproved
    }



    /**
     * Starts the permission check and Geofence process only if the Geofence associated with the
     * current hint isn't yet active.
     */
    private fun checkPermissionsAndOpenPopup(context: Context) {
        if (foregroundLocationPermissionApproved(context)) {
            checkDeviceLocationSettingsAndStartPopup(context)
        } else {
            requestForegroundLocationPermissions()
        }
    }

    /*
   *  Uses the Location Client to check the current state of location settings, and gives the user
   *  the opportunity to turn on location services within our app.
   */
    private fun checkDeviceLocationSettingsAndStartPopup(context: Context, resolve: Boolean = true) {
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
                    checkDeviceLocationSettingsAndStartPopup(context)
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("Devicelocation", "Device location enabled")
                showPopupForReminder()
            }
        }
    }

    fun showPopupForReminder()
    {
        getActivity()?.let { it1 -> ShowPopupWindow().show(it1.supportFragmentManager, "popup") }

    }


    /*
    *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
    */
    @TargetApi(29)
    private fun requestForegroundLocationPermissions() {
        if (foregroundLocationPermissionApproved(thiscontext))
            return

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(
                thiscontext,
                R.string.permission_denied_explanation, Toast.LENGTH_SHORT
            )
                .show()
        } else {
            try {
                   requestPermissionsResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            catch(ex: java.lang.Exception)
            {
                Log.d("Permission",ex.message.toString())
            }

        }
        Log.d("Permission", "Request foreground and background only location permission")

    }



    fun setUpTabs(fldItems:MutableList<FieldItem>,title:String,_feature:Feature,featureType:String){
       // addIdentifyView()
        tabLayout.removeAllTabs()
       when(featureType.uppercase()){
           "POLYLINE" ->
           {
               tabLayout!!.addTab(tabLayout!!.newTab().setText("Feature"))
               tabLayout!!.addTab(tabLayout!!.newTab().setText("Profile"))
           }
           else ->{
               tabLayout!!.addTab(tabLayout!!.newTab().setText("Feature"))
           }


       }
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        val identifyAdapter = IdentifyFragmentAdapter("Identify",fldItems,_feature,requireActivity(),tabLayout!!.tabCount)//ViewPagerAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = identifyAdapter
        val saveReminderBtn = viewOfLayoutresults.findViewById<FloatingActionButton>(R.id.saveReminder)
        saveReminderBtn.fadeIn()

        tabLayout!!.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab:TabLayout.Tab)
            {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setupMap(_itemId:String,_title:String) {

        // create a map with the BasemapStyle streets
        //val map = ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)

        // set the map to be displayed in the layout's MapView
        // mapView.map = map
//
        val _portalUrl = PortalSearch.getPortalUrl()
        val portal = Portal(_portalUrl, false)

        val itemId = _itemId//"4ab2c027c1a14ca0b67ede51c7bcf606"
         portalItem = PortalItem(portal, itemId)
        val map = ArcGISMap(portalItem)
       // (activity as AppCompatActivity).setSupportActionBar(toolbar_name)
        (activity as AppCompatActivity).supportActionBar?.title = _title
        mapView.resume()
        mapView.apply {
            this.map = map

            // add a listener to detect taps on the map view
            onTouchListener = object : DefaultMapViewOnTouchListener(thiscontext, this) {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    e?.let {
                        val screenPoint = android.graphics.Point(
                            it.x.toInt(),
                            it.y.toInt()
                        )
                        identifyResult(screenPoint)
                    }
                    return true
                }
            }

        }

    }

    /**
     * Performs an identify on layers at the given screenpoint and calls handleIdentifyResults(...) to process them.
     *
     * @param screenPoint in Android graphic coordinates.
     */
    private fun identifyResult(screenPoint: android.graphics.Point) {

        val identifyLayerResultsFuture = mapView
            .identifyLayersAsync(screenPoint, 12.0, false, 10)
        // create a map point from screen point

        val mapPoint: com.esri.arcgisruntime.geometry.Point? = mapView.screenToLocation(screenPoint)
        // convert to WGS84 for lat/lon format

        val wgs84Point:com.esri.arcgisruntime.geometry.Point =
            GeometryEngine.project(mapPoint, SpatialReferences.getWgs84()) as com.esri.arcgisruntime.geometry.Point
        // format output
        lat = wgs84Point.y
        long = wgs84Point.x
        _viewModel.longitude.postValue(long)
        _viewModel.latitude.postValue(lat)

        Log.d(
            TAG,
            "Lat: " + java.lang.String.format(
                "%.4f",
                wgs84Point.y
            ) + ", Lon: " + java.lang.String.format("%.4f", wgs84Point.x)
        )

        identifyLayerResultsFuture.addDoneListener {
            try {
                val identifyLayerResults = identifyLayerResultsFuture.get()
                handleIdentifyResults(identifyLayerResults)
            } catch (e: Exception) {
                logError("Error identifying results ${e.message}")
            }
        }
    }

    private fun populateIdentifyProperties(_feature:Feature,layerName:String,popup:Popup):MutableList<FieldItem>
    {
         val featureTable = _feature.featureTable;
        val _popupDefinition = popup.popupDefinition

        val _fields = _popupDefinition.fields
        val _visibleFields:MutableList<PopupField> = mutableListOf<PopupField>()
        val _flditems:MutableList<FieldItem> = mutableListOf<FieldItem>()
        for(field in _fields)
        {
            if(field.isVisible) {
                _visibleFields.add(field)

                val _fldval:String = popup.getFormattedValue(field)
                var _popupField = FieldItem(field.label,_fldval);
                _flditems.add(_popupField)

            }
        }
        return _flditems;


    }

    /**
     * Processes identify results into a string which is passed to showAlertDialog(...).
     *
     * @param identifyLayerResults a list of identify results generated in identifyResult().
     */
    private fun handleIdentifyResults(identifyLayerResults: List<IdentifyLayerResult>) {
        val message = StringBuilder()
        var totalCount:Int = 0
        var mFieldLabelList:List<FieldItem>
        //val mRecyclerView1: RecyclerView = viewOfLayoutresults.findViewById(R.id.recycler_view1)
       // androidx.core.view.ViewCompat.setNestedScrollingEnabled(mRecyclerView1, true);

        for (identifyLayerResult in identifyLayerResults) {
            if(totalCount === 0) {
                mapView.graphicsOverlays.clear()
                val count = recursivelyCountIdentifyResultsForSublayers(identifyLayerResult)
                val layerName = identifyLayerResult.layerContent.name
                val featureLayer: FeatureLayer? =
                    identifyLayerResult.layerContent as? FeatureLayer
                // get the elements in the selection that are features
                val identifiedFeatures = identifyLayerResult.elements.filterIsInstance<Feature>()
                //selectedFeatures.addAll(identifiedFeatures)
                // featureLayer?.selectFeature(identifyLayerResult.popups.first().geoElement as Feature)
                val _feature = identifyLayerResult.popups.first().geoElement as Feature
                featureLayer?.clearSelection()
                featureLayer?.selectFeature(_feature)
                val fldItems = populateIdentifyProperties(
                    _feature,
                    layerName,
                    identifyLayerResult.popups.first()
                )
                val _title = identifyLayerResult.popups.first().title
                val _featureType = featureLayer?.featureTable?.geometryType//"Point"
               // addIdentifyView()
                setUpTabs(fldItems,_title,_feature,_featureType.toString())
                (view?.height)?.div(2)?.let { behavior.setPeekHeight(it) }
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED//STATE_EXPANDED

                // add new line character if not the final element in array
                if (identifyLayerResult != identifyLayerResults[identifyLayerResults.size - 1]) {
                    message.append("\n")
                }
                totalCount += count
            }
        }

        // if any elements were found show the results, else notify user that no elements were found
        if (totalCount > 0) {
            //showAlertDialog(message)
        } else {
            logError("No element found")
        }
    }



    /**
     * Gets a count of the GeoElements in the passed result layer.
     * This method recursively calls itself to descend into sublayers and count their results.
     * @param result from a single layer.
     * @return the total count of GeoElements.
     */
    private fun recursivelyCountIdentifyResultsForSublayers(result: IdentifyLayerResult): Int {
        var subLayerGeoElementCount = 0

        for (sublayerResult in result.sublayerResults) {
            // recursively call this function to accumulate elements from all sublayers
            subLayerGeoElementCount += recursivelyCountIdentifyResultsForSublayers(sublayerResult)
        }

        return subLayerGeoElementCount + result.elements.size
        //add the elements to a list
        //get the visible fields

    }

    /**
     * Log an error to logcat and to the screen via Toast.
     * @param message the text to log.
     */
    private fun logError(message: String?) {
        message?.let {
            Log.e(
                TAG,
                message
            )
            Toast.makeText(thiscontext, message, Toast.LENGTH_LONG).show()
        }
    }
    private fun setApiKeyForApp(){
        // set your API key
        // Note: it is not best practice to store API keys in source code. The API key is referenced
        // here for the convenience of this tutorial.

        ArcGISRuntimeEnvironment.setApiKey("AAPK149ae920390b48c0b654a648850914d7W3ehdaiUusJieDoBWWSTs0uwNdPGpNNIwjSEMbHmEG3l1drVT8gq3qI3Ii-LRNJE")

    }
    override fun onPause() {
        mapView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onDestroy() {
        mapView.dispose()
        super.onDestroy()
    }


    private companion object {
        private val TAG: String = "MapFragment"
    }
}