package com.example.android.mapviewer.data


import com.squareup.moshi.Json

data class GeometryResp(@Json(name = "hasZ")
                        val hasZ: Boolean = false,
                        @Json(name = "hasM")
                        val hasM: Boolean = false,
                        @Json(name = "paths")
                        val paths: List<List<List<Double>>>?)


data class ElevationResponseClass(@Json(name = "results")
                                  val results: List<ResultsItemResp>?)


data class FeaturesItemResp(@Json(name = "attributes")
                            val attributes: AttributesResp,
                            @Json(name = "geometry")
                            val geometry: GeometryResp)


data class ResultsItemResp(@Json(name = "dataType")
                           val dataType: String = "",
                           @Json(name = "paramName")
                           val paramName: String = "",
                           @Json(name = "value")
                           val value: ValueResp)


data class FieldsItemResp(@Json(name = "name")
                          val name: String = "",
                          @Json(name = "alias")
                          val alias: String = "",
                          @Json(name = "type")
                          val type: String = "")


data class AttributesResp(@Json(name = "OBJECTID")
                          val objectid: Int = 0,
                          @Json(name = "DEMResolution")
                          val dEMResolution: String = "",
                          @Json(name = "ProductName")
                          val productName: String = "",
                          @Json(name = "Shape_Length")
                          val shapeLength: Double = 0.0,
                          @Json(name = "Source_URL")
                          val sourceURL: String = "",
                          @Json(name = "ProfileLength")
                          val profileLength: Double = 0.0,
                          @Json(name = "Source")
                          val source: String = "")


data class SpatialReferenceResp(@Json(name = "latestWkid")
                                val latestWkid: Int = 0,
                                @Json(name = "wkid")
                                val wkid: Int = 0)


data class ValueResp(@Json(name = "hasZ")
                     val hasZ: Boolean = false,
                     @Json(name = "features")
                     val features: List<FeaturesItem>?,
                     @Json(name = "hasM")
                     val hasM: Boolean = false,
                     @Json(name = "exceededTransferLimit")
                     val exceededTransferLimit: Boolean = false,
                     @Json(name = "displayFieldName")
                     val displayFieldName: String = "",
                     @Json(name = "spatialReference")
                     val spatialReference: SpatialReferenceResp,
                     @Json(name = "fields")
                     val fields: List<FieldsItem>?,
                     @Json(name = "geometryType")
                     val geometryType: String = "")


