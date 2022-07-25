package com.example.android.mapviewer.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
    data class FieldItem (
        var fieldlabel: String,
        var fieldvalue:String
):Parcelable