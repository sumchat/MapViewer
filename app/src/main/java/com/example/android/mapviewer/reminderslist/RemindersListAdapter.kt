package com.example.android.mapviewer.locationreminders.reminderslist

import android.util.Log
import android.view.View
import com.example.android.mapviewer.R
import com.example.android.mapviewer.locationreminders.ReminderDataItem

import com.example.android.mapviewer.locationreminders.BaseRecyclerViewAdapter
import com.example.android.mapviewer.locationreminders.DataBindingViewHolder


//Use data binding to show the reminder on the item
class RemindersListAdapter(
    val callBack: (ReminderDataItem, String) -> Unit
                           ) :
    BaseRecyclerViewAdapter<ReminderDataItem>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.it_reminder
    override fun onBindViewHolder(holder: DataBindingViewHolder<ReminderDataItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            zoomtoLocation(item)
        }



        val button: View =  holder.itemView.findViewById(R.id.delete_button)
        button.setOnClickListener{
            Log.e("buttonclick",item.id)
            deleteReminder( item)

        }


    }


    fun zoomtoLocation(item:ReminderDataItem)
    {
        callBack?.invoke(item,"zoom")
    }

    fun deleteReminder(item:ReminderDataItem)
    {
        callBack?.invoke(item,"delete")
    }



}