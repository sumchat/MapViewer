package com.example.android.mapviewer.data

import org.json.JSONArray

data class GeometryItem(
    val paths: List<List<List<Double>>>?,
    val spatialReference: SpatialReference1?)


data class FeaturesItem(val geometry: GeometryItem)


data class FieldsItem(val name: String = "",
                      val alias: String = "",
                      val type: String = "")


data class SpatialReference(val latestWkid: Int = 0,
                            val wkid: Int = 0)


data class Attributes(val OID: Int = 0)


data class InputLineFeaturesObj(val features: List<FeaturesItem>?,
                                 val attributes: Attributes,
                                 val fields: List<FieldsItem>?,
                                 val geometryType: String = "",
                                 val spatialReference: SpatialReference)


data class Sr(val latestWkid: Int = 0,
              val wkid: Int = 0)


