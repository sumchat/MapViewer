package com.example.android.mapviewer.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSource_Attachments (resources: Resources) {
    //private val attributeList = attributeList(resources)
    private  val attachmentsLiveData = MutableLiveData<List<Attachment>>()

    fun addAttachment(attachment: Attachment) {
        val currentList = attachmentsLiveData.value
        if (currentList == null) {
            attachmentsLiveData.postValue(listOf(attachment))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, attachment)
            attachmentsLiveData.postValue(updatedList)
        }
    }
    fun getAttachmentList(): LiveData<List<Attachment>> {
        return attachmentsLiveData
    }

    companion object {
        private var INSTANCE: DataSource_Attachments? = null

        fun getDataSource(resources: Resources): DataSource_Attachments {
            return synchronized(DataSource_Attachments::class) {
                val newInstance = INSTANCE ?: DataSource_Attachments(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}