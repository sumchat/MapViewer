package com.example.android.mapviewer.data

import androidx.annotation.DrawableRes

data class MapItem (
    val id: Long,
    var name: String,
    var author:String,
    var creationDate:String,
    var size:Int,
    @DrawableRes
    val image: Int?,
    var description: String
)