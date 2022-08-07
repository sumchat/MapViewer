package com.example.android.mapviewer

import android.os.Bundle
import android.util.Log
import com.esri.arcgisruntime.portal.Portal
import android.widget.Toast

import android.view.Gravity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import com.esri.arcgisruntime.portal.PortalItem

import com.esri.arcgisruntime.portal.PortalQueryResultSet

import com.esri.arcgisruntime.concurrent.ListenableFuture

import com.esri.arcgisruntime.portal.PortalQueryParameters
import java.lang.Exception
import androidx.recyclerview.widget.RecyclerView
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.example.android.mapviewer.data.MapItem


class PortalSearch {
    val portal:Portal = Portal("https://www.arcgis.com", false)
    private var mPortalQueryResultSet: PortalQueryResultSet<PortalItem>? = null
   // public lateinit var mPortalItemList:List<PortalItem>
    private val mRecyclerView: RecyclerView? = null
    private  var mMapItems:List<MapItem> = emptyList()


    fun findItems()
    {

    }

    private fun adapterOnClick(mapItem: MapItem) {
        //val navController = NavHostFragment.findNavController(this)
        //this.findNavController().navigate()
        // val intent = Intent(thiscontext, ShoeDetailFragment()::class.java)
        // intent.putExtra(SHOE_ID, shoe.id)
        // startActivity(intent)
        //this.findNavController().navigate(MyShoeListFragmentDirections.actionMyShoeListFragmentToShoeDetailFragment2(shoe.id))
        // findNavController().navigate(MyShoeListFragmentDirections.actionMyShoeListFragmentToShoeDetailFragment(shoe.id))
    }

    private fun search(keyword: String) {
        // create query parameters specifying the type webmap
        val params = PortalQueryParameters()
        params.setQuery(PortalItem.Type.WEBMAP, "Appstudio632", keyword)
        val portalUrl = getPortalUrl()
        // find matching portal items. This search may field a large number of results (limited to 10 be default). Set the
        // results limit field on the query parameters to change the default amount.
        val results: ListenableFuture<PortalQueryResultSet<PortalItem>> =
            portal.findItemsAsync(params)
        results.addDoneListener {
            try {
                // hide search instructions
                //mSearchInstructionsTextView.setVisibility(View.GONE)
                // update the results list view with matching items
                mPortalQueryResultSet = results.get()
               // left?.let { queue.add(it) }
                mPortalItemList = mPortalQueryResultSet?.let{it.getResults()} as List<PortalItem>
                mMapItems = mPortalItemList.map {

                    MapItem(it.itemId,it.title,it.owner,it.created,it.size,portalUrl.plus("/").plus(it.itemId).plus("/info/thumbnail/ago_downloaded.png"))
                }
               /* val portalItemAdapter:PortalItemAdapter = PortalItemAdapter (mMapItemList!!, { mapItem ->
                    adapterOnClick(mapItem)}
                )*/
                val portalItemAdapter:PortalItemAdapter = PortalItemAdapter ( mMapItems, {mapItem ->
                    adapterOnClick(mapItem)}
                )


                if (mRecyclerView != null) {
                    mRecyclerView.setAdapter(portalItemAdapter)
                }
                // open the drawer once there are results
               // mDrawer.openDrawer(Gravity.START)
                // get 10 more results to fill the recycler view
               // getMoreResults()
            } catch (e: Exception) {
                val error = "Error getting portal query result set: " + e.message
               // Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                //Log.e(TAG, error)
            }
        }
    }

    /**
     * Add the given portal item to a new map and set the map to the map view.
     *
     * @param portal
     * @param itemId
     */
    private fun addMap(portal: Portal?, itemId: String) {
        // report error and return if portal is null
        if (portal == null) {
            val error = "Portal not instantiated."
           // Toast.makeText(this, error, Toast.LENGTH_LONG).show()
           // Log.e(TAG, error)
            return
        }
        // use the item ID to create a portal item from the portal
        val portalItem = PortalItem(portal, itemId)
        // create a map using the web map (portal item) and add it to the map view
       /* val webMap = ArcGISMap(portalItem)
        mMapView.setMap(webMap)
        // close the drawer
        mDrawer.closeDrawer(Gravity.START)
        // check if webmap is supported
        mMapView.getMap().addDoneLoadingListener {
            if (mMapView.getMap().getLoadError() != null) {
                val error =
                    "Unable to load map: " + mMapView.getMap().getLoadError().getMessage()
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                Log.e(TAG, error)
            }
        }
   */
    }

    companion object {

      private lateinit var mPortalItemList: List<PortalItem>

     fun getPortalItems():List<PortalItem>
     {

         return mPortalItemList
     }
        fun getPortalUrl():String
        {
            return "https://www.arcgis.com"
        }
    }


}