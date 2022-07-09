package com.example.android.mapviewer

import androidx.lifecycle.ViewModel
import com.example.android.mapviewer.data.Attribute
import com.example.android.mapviewer.data.DataSource_Attributes
import javax.sql.DataSource

class AttributeListViewModel (val dataSource: DataSource_Attributes): ViewModel() {
    val attributesLiveData = dataSource.getAttributeList()
    fun insertAttribute(fieldName: String?, fieldValue: String) {
        if (fieldName == null ) {
            return
        }
       // val image = dataSource.getRandomShoeImageAsset()
        val newAttribute = Attribute(fieldName,fieldValue)
        dataSource.addAttribute(newAttribute)
    }
}