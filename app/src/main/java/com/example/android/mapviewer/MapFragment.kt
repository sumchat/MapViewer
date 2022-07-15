package com.example.android.mapviewer

import ViewPagerAdapter
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.data.Feature
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.popup.Popup
import com.esri.arcgisruntime.mapping.popup.PopupField
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.portal.Portal
import com.esri.arcgisruntime.portal.PortalItem
import com.example.android.mapviewer.databinding.FragmentMappageBinding
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult
import com.example.android.mapviewer.data.FieldItem
import com.google.android.material.tabs.TabLayout
import kotlin.math.roundToInt
//import com.esri.arcgisruntime.toolkit.popup.PopupViewModel


class MapFragment : Fragment(){//AppCompatActivity() {
    lateinit var thiscontext: Context
    private lateinit var viewOfLayout: View
    private lateinit var viewOfLayoutresults: View
    private lateinit var mRecyclerView1: RecyclerView
    private lateinit var headerText: TextView
    private lateinit var tabView:View
    private lateinit var viewPager: ViewPager2 // creating object of ViewPager
    private lateinit var tabLayout: TabLayout

    private lateinit  var portalItem:PortalItem
    private val selectedFeatures by lazy { ArrayList<Feature>() }
   // private val popupViewModel: PopupViewModel by viewModels()
   private lateinit var _fieldItemAdapter:FieldValueAdapter

    private val activityMainBinding by lazy {
        FragmentMappageBinding.inflate(layoutInflater)
    }

    private val mapView: MapView by lazy {
        viewOfLayoutresults.findViewById(R.id.mapView)
        //activityMainBinding.mapView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun adapterOnClick(fieldItem: FieldItem) {
        //itemId  = portalItem.itemId
        //val action = MapGalleryFragmentDirections.actionMapGalleryFragmentToMapFragment(itemId)
        //findNavController().navigate(action)

          }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //activityMainBinding.
        // return inflater.inflate(R.layout.fragment_instruction, container, false)
        //var bBinding : ViewDataBinding? = DataBindingUtil.inflate(inflater,R.layout.fragment_instruction,container,false)

       //var bundle:Bundle? = getArguments()
       //var portalItem:PortalItem = bundle.get(portalItem) as PortalItem
        if (container != null) {
            thiscontext = container.getContext()
        }
        viewOfLayoutresults = inflater.inflate(R.layout.fragment_mappage, container, false)
        // mRecyclerView1 = viewOfLayoutresults.findViewById(R.id.recycler_view1)
        //headerText = viewOfLayoutresults.findViewById(R.id.textView3)


        tabView = viewOfLayoutresults.findViewById<View>(R.id.identify)//.visibility = View.VISIBLE

        tabLayout = viewOfLayoutresults.findViewById<TabLayout>(R.id.tab_layout)
        viewPager = viewOfLayoutresults.findViewById<ViewPager2>(R.id.pager)




        var args = MapFragmentArgs.fromBundle(requireArguments())
       // val _itemId = getArguments()?.getString("itemId")
        val _itemId: String = args.myArg
        //portalItem = args.itemId
        setApiKeyForApp()

        setupMap(_itemId)
        //viewOfLayout = inflater.inflate(R.layout.fieldvalue_row, container, false)
        return viewOfLayoutresults // activityMainBinding.root

    }

    fun setUpTabs(fldItems:MutableList<FieldItem>,title:String,_feature:Feature,featureType:String){
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
        val identifyAdapter = IdentifyFragmentAdapter(fldItems,_feature,requireActivity(),tabLayout!!.tabCount)//ViewPagerAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = identifyAdapter

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

    private fun setupMap(_itemId:String) {

        // create a map with the BasemapStyle streets
        //val map = ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)

        // set the map to be displayed in the layout's MapView
        // mapView.map = map
//
        val _portalUrl = PortalSearch.getPortalUrl()
        val portal = Portal(_portalUrl, false)

        val itemId = _itemId//"4ab2c027c1a14ca0b67ede51c7bcf606"
        val portalItem = PortalItem(portal, itemId)
        val map = ArcGISMap(portalItem)
        mapView.apply {
            this.map = map

            // add a listener to detect taps on the map view
            onTouchListener = object : DefaultMapViewOnTouchListener(thiscontext, this) {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    e?.let {
                        val screenPoint = Point(
                            it.x.roundToInt(),
                            it.y.roundToInt()
                        )
                        identifyResult(screenPoint)
                    }
                    return true
                }
            }
        }

        //mapView.map = map

        // set the viewpoint, Viewpoint(latitude, longitude, scale)
        //  mapView.setViewpoint(Viewpoint(34.0270, -118.8050, 72000.0))



    }

    /**
     * Performs an identify on layers at the given screenpoint and calls handleIdentifyResults(...) to process them.
     *
     * @param screenPoint in Android graphic coordinates.
     */
    private fun identifyResult(screenPoint: Point) {

        val identifyLayerResultsFuture = mapView
            .identifyLayersAsync(screenPoint, 12.0, false, 10)

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
                val count = recursivelyCountIdentifyResultsForSublayers(identifyLayerResult)
                val layerName = identifyLayerResult.layerContent.name
                val featureLayer: FeatureLayer? =
                    identifyLayerResult.layerContent as? FeatureLayer
                // get the elements in the selection that are features
                val identifiedFeatures = identifyLayerResult.elements.filterIsInstance<Feature>()
                //selectedFeatures.addAll(identifiedFeatures)
                // featureLayer?.selectFeature(identifyLayerResult.popups.first().geoElement as Feature)
                val _feature = identifyLayerResult.popups.first().geoElement as Feature

                featureLayer?.selectFeature(_feature)
                val fldItems = populateIdentifyProperties(
                    _feature,
                    layerName,
                    identifyLayerResult.popups.first()
                )
                val _title = identifyLayerResult.popups.first().title
                val _featureType = featureLayer?.featureTable?.geometryType//"Point"

                setUpTabs(fldItems,_title,_feature,_featureType.toString())




                // add the features to the current feature layer selection
                // featureLayer?.selectFeatures(identifiedFeatures)
                // message.append(layerName).append(": ").append(count)

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

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_mappage)
        var container = findViewById(R.id.container) as LinearLayout
        var params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //params.setMargins(layout_marginTop(10), dpTopx(10), dpTopx(10), dpTopx(10))
        params.setMargins(10,10,10,10)
        container.setLayoutParams(params)
        container.setOrientation(LinearLayout.VERTICAL)
       // container.addView(l);
        //var mainTabFragment : Fragment? = null
       // mainTabFragment = MainTabFragment.newInstance()
       // container.addView(mainTabFragment.view)
    }*/

    /*private fun setUpToolbar() {

        val mainActivity = mActivity as MainActivity
        val navigationView: NavigationView = mActivity.findViewById(R.id.navView)
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = mainActivity.appBarConfiguration
        NavigationUI.setupActionBarWithNavController(mainActivity,navController,appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView,navController)

    }*/
    private companion object {
        private val TAG: String = "MapFragment"
    }
}