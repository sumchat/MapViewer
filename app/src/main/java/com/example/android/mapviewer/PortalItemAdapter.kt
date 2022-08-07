package com.example.android.mapviewer

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.AdapterView

import com.esri.arcgisruntime.portal.PortalItem

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.widget.ImageView
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.portal.PortalQueryResultSet
import com.example.android.mapviewer.data.MapItem
import com.example.android.mapviewer.databinding.WebmapRowBinding
import java.lang.Exception


/*class PortalItemAdapter(
    private val mMapItemList: List<MapItem>,
    private val onClick:(MapItem)-> Unit
    //private val mOnItemClickListener: (Any) -> Unit
) :
    ListAdapter<MapItem, PortalItemAdapter.PortalItemViewHolder>(PortalItemDiffCallback) {
    interface OnItemClickListener {
        fun onItemClick(mapItem: MapItem?)
    }
    // Define listener member variable
    private lateinit var listener: AdapterView.OnItemClickListener
    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
        this.listener = listener
    }

    class PortalItemViewHolder(itemView: View,val onClick:(PortalItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val mPortalItemTextView: TextView = itemView.findViewById(R.id.webmapItem)
        val mThumbnailImageView:ImageView = itemView.findViewById(R.id.mapThumbnail)
        private var currentPortalItem: PortalItem? = null
        private lateinit var mThumbnail:Bitmap
        init {
            // mPortalItemTextView = itemView.findViewById(R.id.webmapItem)

            /* itemView.setOnClickListener {
                 // Triggers click upwards to the adapter on click
                 currentPortalItem?.let {
                     onClick(it)
                 }
             }*/
        }

        fun bind(portalItem: PortalItem) {//, listener: OnItemClickListener) {
            mPortalItemTextView.text = portalItem.title
            mPortalItemTextView.setOnClickListener{
                // Triggers click upwards to the adapter on click

                portalItem?.let {
                    onClick(it)
                }
            }

        // https://melbournedev.maps.arcgis.com/sharing/rest/content/items/1115bd68a6f54bcfa38bc5a696d2862a/info/thumbnail/ago_downloaded.png

           /* val results = portalItem.fetchThumbnailAsync()
            results.addDoneListener{
                mThumbnail = BitmapFactory.decodeByteArray(

                    results.get(),
                    0,
                    results.get().size
                )
                mThumbnailImageView.setImageBitmap(mThumbnail)

            }*/


        }

        /* init {
            mPortalItemTextView = itemView.findViewById(R.id.webmapItem)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortalItemViewHolder {
        val portalItemTextView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.webmap_row, parent, false)
        return PortalItemViewHolder(portalItemTextView,onClick)
    }

    override fun onBindViewHolder(holder: PortalItemViewHolder, position: Int) {
        holder.bind(mPortalItemList[position])//, mOnItemClickListener)
        //val portalItem = getItem(position)
        //holder.bind(portalItem)
    }

    override fun getItemCount(): Int {
        return mPortalItemList.size
    }
}



object PortalItemDiffCallback : DiffUtil.ItemCallback<PortalItem>() {
    override fun areItemsTheSame(oldItem: PortalItem, newItem: PortalItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PortalItem, newItem: PortalItem): Boolean {
        return oldItem.itemId == newItem.itemId

    }
}*/
/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class PortalItemAdapter(private val mMapItemList: List<MapItem>,
                        private val onClick:(MapItem)-> Unit ) :
    ListAdapter<MapItem, PortalItemAdapter.MapItemViewHolder>(DiffCallback) {
    /**
     * The MarsPropertyViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */
    class MapItemViewHolder(private var binding: WebmapRowBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mapItem: MapItem) {
            binding.property = mapItem
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<MapItem>() {
        override fun areItemsTheSame(oldItem: MapItem, newItem: MapItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MapItem, newItem: MapItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MapItemViewHolder {
        return MapItemViewHolder(WebmapRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: MapItemViewHolder, position: Int) {
        val mapItem = mMapItemList[position]//getItem(position)
        holder.itemView.setOnClickListener {
            //onClickListener.onClick(marsProperty)
            onClick(mapItem)
        }
        holder.bind(mapItem)
    }
    override fun getItemCount(): Int {
        return mMapItemList.size
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [MarsProperty]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [MarsProperty]
     */
    class OnClickListener(val clickListener: (mapItem:MapItem) -> Unit) {
        fun onClick(mapItem:MapItem) = clickListener(mapItem)
    }
}
