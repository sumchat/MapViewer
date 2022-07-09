package com.example.android.mapviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mapviewer.data.Attachment

class AttachmentAdapter  (private val onClick:(Attachment)-> Unit):
    ListAdapter<Attachment, AttachmentAdapter.AttachmentViewHolder>(AttachmentDiffCallback) {

    // Define the listener interface
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    // Define listener member variable
    private lateinit var listener: AdapterView.OnItemClickListener

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
        this.listener = listener
    }

    class AttachmentViewHolder(itemView: View, val onClick:(Attachment) -> Unit): RecyclerView.ViewHolder(itemView) {
        private val attachmentNameView: TextView = itemView.findViewById(R.id.attachmentName_text)
        // private val shoeImageView: ImageView = itemView.findViewById(R.id.shoe_image)
        private var currentAttachment: Attachment? = null
        /*init {
            itemView.setOnClickListener {
                // Triggers click upwards to the adapter on click
                currentAttribute?.let{
                    onClick(it)
                }
            }
        }*/



        /* Bind flower name and image. */
        fun bind(attachment: Attachment) {
            currentAttachment = attachment

            attachmentNameView.text = attachment.name

        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attachment, parent, false)
        return AttachmentViewHolder(view,onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val attachment = getItem(position)//mShoes.get(position)
        holder.bind(attachment)

    }

    /* override fun getItemCount(): Int {
         return mShoes.size
     }*/
}

object AttachmentDiffCallback : DiffUtil.ItemCallback<Attachment>() {
    override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
        return oldItem.name == newItem.name
    }
}