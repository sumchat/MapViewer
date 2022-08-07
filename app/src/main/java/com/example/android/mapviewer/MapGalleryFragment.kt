package com.example.android.mapviewer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.portal.Portal
import com.esri.arcgisruntime.portal.PortalItem
import com.esri.arcgisruntime.portal.PortalQueryParameters
import com.esri.arcgisruntime.portal.PortalQueryResultSet
import com.example.android.mapviewer.data.MapItem
import java.lang.Exception
import com.example.android.mapviewer.MapGalleryFragmentDirections as MapGalleryFragmentDirections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [MapGalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapGalleryFragment : Fragment() {
    lateinit var thiscontext: Context
    //private lateinit var viewModel:GalleryViewModel
    private lateinit var viewOfLayout: View
    public lateinit var itemId:String
    private var progressBar: ProgressBar? = null

    val portal: Portal = Portal("https://www.arcgis.com", false)
    private var mPortalQueryResultSet: PortalQueryResultSet<PortalItem>? = null
    private lateinit var mPortalItemList:List<PortalItem>
    private  var mMapItems:List<MapItem> = emptyList()
   // private val mRecyclerView: RecyclerView? = null
    private lateinit var _portalItemAdapter:PortalItemAdapter


    private fun adapterOnClick(mapItem: MapItem) {
        itemId  = mapItem.id
        val action = MapGalleryFragmentDirections.actionMapGalleryFragmentToMapFragment(itemId,mapItem.name)
        findNavController().navigate(action)

        //findNavController().navigate(MapGalleryFragmentDirections.actionMapGalleryFragmentToMapFragment(itemId))

       // this.findNavController().navigate(mapGalleryFragmentDirections.actionMapGalleryFragmentToMapFragment(portalItem.itemId))
        // findNavController().navigate(MyShoeListFragmentDirections.actionMyShoeListFragmentToShoeDetailFragment(shoe.id))
    }

    private val portalItemListViewModel by viewModels<PortalItemListViewModel> {
        PortalItemListViewModelFactory(thiscontext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_map_gallery, container, false)
        // Inflate the layout for this fragment
        if (container != null) {
            thiscontext = container.getContext()
        }
        viewOfLayout = inflater.inflate(R.layout.fragment_map_gallery, container, false)
       // val mRecyclerView: RecyclerView = viewOfLayout.findViewById(R.id.recycler_view)
        // finding progressbar by its id
        progressBar = viewOfLayout.findViewById<ProgressBar>(R.id.progressBar) as ProgressBar
        progressBar!!.visibility = View.VISIBLE
        search("Appstudio632")


        setHasOptionsMenu(true)


        //val fab:View = viewOfLayout.findViewById(R.id.fab)
        //fab.setOnClickListener{v:View -> v.findNavController().navigate(MyShoeListFragmentDirections.actionMyShoeListFragmentToShoeDetailFragment2(maxId.plus(1)))}

        return viewOfLayout
    }

    private fun search(keyword: String) {
        // create query parameters specifying the type webmap
        val mRecyclerView: RecyclerView = viewOfLayout.findViewById(R.id.recycler_view)
        val params = PortalQueryParameters()
        val portalUrl =PortalSearch.getPortalUrl()
        params.setQuery(PortalItem.Type.WEBMAP, "", keyword)
        // find matching portal items. This search may field a large number of results (limited to 10 be default). Set the
        // results limit field on the query parameters to change the default amount.
        val results: ListenableFuture<PortalQueryResultSet<PortalItem>> =
            portal.findItemsAsync(params)
        results.addDoneListener {
            try {
                // hide search instructions
                //mSearchInstructionsTextView.setVisibility(View.GONE)
                // update the results list view with matching items
                progressBar!!.visibility = View.INVISIBLE
                mPortalQueryResultSet = results.get()
                // left?.let { queue.add(it) }
                mPortalItemList = mPortalQueryResultSet?.let{it.getResults()} as List<PortalItem>

                mMapItems = mPortalItemList.map {

                    MapItem(it.itemId,it.title,it.owner,it.created,it.size,portalUrl.plus("/sharing/rest/content/items/").plus(it.itemId).plus("/info/thumbnail/ago_downloaded.png"))
                }


               /* _portalItemAdapter = PortalItemAdapter ( mPortalItemList, {portalItem ->
                    adapterOnClick(portalItem)}
                )*/

                _portalItemAdapter = PortalItemAdapter ( mMapItems, {mapItem ->
                    adapterOnClick(mapItem)}
                )


                if (mRecyclerView != null) {
                    mRecyclerView.adapter = _portalItemAdapter
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



}