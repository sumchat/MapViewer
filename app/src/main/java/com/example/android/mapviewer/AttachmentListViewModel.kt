package com.example.android.mapviewer

import androidx.lifecycle.ViewModel
import com.example.android.mapviewer.data.Attachment
import com.example.android.mapviewer.data.DataSource_Attachments

class AttachmentListViewModel (val dataSource: DataSource_Attachments): ViewModel() {
    val attachmentsLiveData = dataSource.getAttachmentList()
    fun insertAttachment(fieldName: String?, fieldValue: String) {
        if (fieldName == null) {
            return
        }
        // val image = dataSource.getRandomShoeImageAsset()
        val newAttachment = Attachment(fieldName)
        dataSource.addAttachment(newAttachment)
    }
}