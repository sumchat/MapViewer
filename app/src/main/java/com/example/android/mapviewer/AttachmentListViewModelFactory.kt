package com.example.android.mapviewer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.mapviewer.data.DataSource_Attachments
import com.example.android.mapviewer.data.DataSource_Attributes

class AttachmentListViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttributeListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AttachmentListViewModel(
                dataSource = DataSource_Attachments.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}