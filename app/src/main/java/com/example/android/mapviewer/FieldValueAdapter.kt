package com.example.android.mapviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.esri.arcgisruntime.portal.PortalItem
import com.example.android.mapviewer.data.FieldItem

 //class FieldValueAdapter(private val mFieldList: List<FieldItem>,
  //                       private val onClick:(FieldItem)-> Unit):
class FieldValueAdapter():
     ListAdapter<FieldItem, FieldValueAdapter.FieldItemViewHolder>(FieldItemDiffCallback) {
    // var fieldItems:List<FieldItem> = mFieldList;

     interface OnItemClickListener {
         fun onItemClick(fieldItem: FieldItem?)
     }
     // Define listener member variable
     private lateinit var listener: AdapterView.OnItemClickListener



     // Define the method that allows the parent activity or fragment to define the listener
     fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
         this.listener = listener
     }

    //class FieldItemViewHolder(itemView: View, val onClick:(FieldItem) -> Unit): RecyclerView.ViewHolder(itemView) {
    class FieldItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val fieldlabelView: TextView = itemView.findViewById(R.id.fieldLabel)
        val fieldValueView: TextView = itemView.findViewById(R.id.fieldValue)
        var currentField: FieldItem? = null
       /* init {
            itemView.setOnClickListener {
                // Triggers click upwards to the adapter on click
                currentField?.let {
                    onClick(it)
                }
            }
        }*/

        /* Bind fielditem name. */
        fun bind(fieldItem: FieldItem) {
            currentField = fieldItem

            fieldlabelView.text = fieldItem.fieldlabel
            fieldValueView.text = fieldItem.fieldvalue

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fieldvalue_row, parent, false)

        return FieldItemViewHolder(view)
    }
    /* Gets current fielditem and uses it to bind view. */
    override fun onBindViewHolder(holder: FieldItemViewHolder, position: Int) {
        val fieldItem = getItem(position)
        holder.bind(fieldItem)
        //holder.bind(fieldItems[position])


    }
   /*  override fun getItemCount(): Int {
         return fieldItems.size
     }*/
}

object FieldItemDiffCallback : DiffUtil.ItemCallback<FieldItem>() {
    override fun areItemsTheSame(oldItem: FieldItem, newItem: FieldItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FieldItem, newItem: FieldItem): Boolean {
        return oldItem.fieldlabel == newItem.fieldlabel
    }
}
