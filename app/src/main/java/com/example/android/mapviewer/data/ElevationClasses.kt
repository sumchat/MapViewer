package com.example.android.mapviewer.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*@JsonClass(generateAdapter = true)
data class FieldObj(
    @Json(name = "name")
    val name:String,
    @Json(name = "type")
    val type:String,
    @Json(name = "alias")
    val alias:String
)

@JsonClass(generateAdapter = true)
data class OIDObj(
    @Json(name = "OID")
    val OID:Int
)

@JsonClass(generateAdapter = true)
data class InputObj(
    @Json(name = "fields")
    val fields:List<FieldObj>,
    @Json(name = "geometryType")
    val geometryType:String,
    @Json(name="attributes")
    val attributes:OIDObj,
    @Json(name="sr")
    val sr:WkidObj
)



    @JsonClass(generateAdapter = true)
    data class ElevationRequestParam(
        @Json(name = "InputLineFeatures")
        val InputLineFeatures: InputObj,
        @Json(name = "ProfileIDField")
        val ProfileIDField: String,
        @Json(name = "DEMResolution")
        val DEMResolution:String,
        @Json(name = "MaximumSampleDistance")
        val MaximumSampleDistance:String,
        @Json(name = "MaximumSampleDistanceUnits")
        val MaximumSampleDistanceUits:String,
        @Json(name = "returnZ")
        val returnZ:String,
        @Json(name = "returnM")
        val returnM:String,
        @Json(name = "f")
        val f:String

    )

@JsonClass(generateAdapter = true)
data class WkidObj(
    @Json(name="wkid")
    val wkid:Int,
    @Json(name="latestWkid")
    val latestWkid:Int
)
@JsonClass(generateAdapter = true)
data class FeatureAttrib(
    @Json(name="OBJECTID")
    val OBJECTID:Long,
    @Json(name="DEMResolution")
    val DEMResolution: Long,
    @Json(name="ProfileLength")
    val ProfileLength: Double,
    @Json(name="Shape_Length")
    val Shape_Length: Double
)
@JsonClass(generateAdapter = true)
data class PointObj(
    @Json(name="x")
    val x:Double,
    @Json(name="y")
    val y:Double,
    @Json(name="z")
    val z:Double,
    @Json(name="m")
    val m:Double
)

@JsonClass(generateAdapter = true)
data class PathObj(
    @Json(name="paths")
    val paths:List<List<PointObj>>
)

@JsonClass(generateAdapter = true)
data class FeatureAttributes(
    @Json(name="attributes")
    val attributes:FeatureAttrib,
    @Json(name="geometry")
    val geometry:PathObj
)

@JsonClass(generateAdapter = true)
data class FeaturesObj(
    @Json(name="features")
    val features:List<FeatureAttributes>,
    @Json(name="exceededTransferLimit")
    val exceededTransferLimit:Boolean

)





@JsonClass(generateAdapter = true)
data class Responsevalue(
    @Json(name="displayFieldName")
    val displayFieldName:String,
    @Json(name="geometryType")
    val geometryType:String,
    @Json(name="spatialReference")
    val spatialReference:WkidObj,
    @Json(name="fields")
    val fields:List<String>,
    @Json(name="features")
    val features:List<FeatureAttributes>,
    @Json(name="exceededTransferLimit")
    val exceededTransferLimit:Boolean


)


@JsonClass(generateAdapter = true)
data class Dataset(
    @Json(name="paramName")
    val paramName:String,
    @Json(name="dataType")
    val dataType:String,
    @Json(name="value")
    val value:Responsevalue

)



@JsonClass(generateAdapter = true)
    data class ElevationResponse(
    @Json(name="results")
    val results:List<Dataset>,
    @Json(name="messages")
    val messages:List<String>

    )*/


data class FieldObj(

    val name:String,

    val type:String,

    val alias:String
)


data class OIDObj(

    val OID:Int
)


data class InputObj(

    val fields:List<FieldObj>,

    val geometryType:String,

    val attributes:OIDObj,

    val sr:WkidObj
)




data class ElevationRequestParam(

    val InputLineFeatures: InputObj,

    val ProfileIDField: String,

    val DEMResolution:String,

    val MaximumSampleDistance:String,

    val MaximumSampleDistanceUits:String,

    val returnZ:String,

    val returnM:String,

    val f:String

)


data class WkidObj(

    val wkid:Int,

    val latestWkid:Int
)

data class FeatureAttrib(

    val OBJECTID:Long,

    val DEMResolution: Long,

    val ProfileLength: Double,

    val Shape_Length: Double
)
@JsonClass(generateAdapter = true)
data class PointObj(

    val x:Double,

    val y:Double,

    val z:Double,

    val m:Double
)

//@JsonClass(generateAdapter = true)
data class PointObj2(

    val x:Double,

    val y:Double,

    val z:Double


)


data class PathObj(

    val paths:List<List<PointObj>>
)

data class GeomPath(
    val hasZ: Boolean,
    val paths: List<List<PointObj2>>,
    val spatialReference: WkidObj
)
@JsonClass(generateAdapter = true)
data class BaseDataModel (
    val hasZ: Boolean?,
    val paths : ArrayList<ArrayList<ArrayList<Double>>>,
    val spatialReference : SpatialReference1?
)
@JsonClass(generateAdapter = true)
data class SpatialReference1 (
    val wkid : Int? = null,
    val latestWkid : Int?
)


data class FeatureAttributes(

    val attributes:FeatureAttrib,

    val geometry:PathObj
)


data class FeaturesObj(

    val features:List<FeatureAttributes>,

    val exceededTransferLimit:Boolean

)






data class Responsevalue(

    val displayFieldName:String,

    val geometryType:String,

    val spatialReference:WkidObj,

    val fields:List<String>,

    val features:List<FeatureAttributes>,

    val exceededTransferLimit:Boolean


)



data class Dataset(

    val paramName:String,

    val dataType:String,

    val value:Responsevalue

)




data class ElevationResponse(

    val results:List<Dataset>,

    val messages:List<String>

)
