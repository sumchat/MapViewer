package com.example.android.mapviewer.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSource_Attributes(resources: Resources) {
    //private val attributeList = attributeList(resources)
  private  val attributesLiveData = MutableLiveData<List<Attribute>>()

    fun addAttribute(attribute: Attribute) {
        val currentList = attributesLiveData.value
        if (currentList == null) {
            attributesLiveData.postValue(listOf(attribute))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, attribute)
            attributesLiveData.postValue(updatedList)
        }
    }

    fun getAttributeList(): LiveData<List<Attribute>> {
        return attributesLiveData
    }

    companion object {
        private var INSTANCE: DataSource_Attributes? = null

        fun getDataSource(resources: Resources): DataSource_Attributes {
            return synchronized(DataSource_Attributes::class) {
                val newInstance = INSTANCE ?: DataSource_Attributes(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}