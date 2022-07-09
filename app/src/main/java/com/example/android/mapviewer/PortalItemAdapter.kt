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
import java.lang.Exception


class PortalItemAdapter(
    private val mPortalItemList: List<PortalItem>,
    private val onClick:(PortalItem)-> Unit
    //private val mOnItemClickListener: (Any) -> Unit
) :
    ListAdapter<PortalItem, PortalItemAdapter.PortalItemViewHolder>(PortalItemDiffCallback) {
    interface OnItemClickListener {
        fun onItemClick(portalItem: PortalItem?)
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

            val results = portalItem.fetchThumbnailAsync()
                results.addDoneListener{
                    mThumbnail = BitmapFactory.decodeByteArray(

                        results.get(),
                        0,
                        results.get().size
                    )
                    mThumbnailImageView.setImageBitmap(mThumbnail)

                }


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
}