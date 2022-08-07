package com.example.android.mapviewer.data

import androidx.annotation.DrawableRes
import java.util.*

data class MapItem(
    val id: String,
    var name: String,
    var author:String,
    var creationDate: Calendar,
    var size: Long,
    val imageUrl: String

)