package com.example.android.mapviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mapviewer.data.Attribute

class AttributeAdapter (private val onClick:(Attribute)-> Unit):
    ListAdapter<Attribute, AttributeAdapter.AttributeViewHolder>(AttributeDiffCallback) {

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

    class AttributeViewHolder(itemView: View, val onClick:(Attribute) -> Unit): RecyclerView.ViewHolder(itemView) {
        private val attributeNameView: TextView = itemView.findViewById(R.id.field_text)
       // private val shoeImageView: ImageView = itemView.findViewById(R.id.shoe_image)
        private var currentAttribute: Attribute? = null
        /*init {
            itemView.setOnClickListener {
                // Triggers click upwards to the adapter on click
                currentAttribute?.let{
                    onClick(it)
                }
            }
        }*/



        /* Bind flower name and image. */
        fun bind(attribute: Attribute) {
            currentAttribute = attribute

            attributeNameView.text = attribute.name

        }
    }

    /* Creates and inflates view and return FlowerViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attribute, parent, false)
        return AttributeViewHolder(view,onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        val shoe = getItem(position)//mShoes.get(position)
        holder.bind(shoe)

    }

    /* override fun getItemCount(): Int {
         return mShoes.size
     }*/
}

object AttributeDiffCallback : DiffUtil.ItemCallback<Attribute>() {
    override fun areItemsTheSame(oldItem: Attribute, newItem: Attribute): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Attribute, newItem: Attribute): Boolean {
        return oldItem.name == newItem.name
    }
}