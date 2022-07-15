package com.example.android.mapviewer.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esri.arcgisruntime.data.Feature
import com.esri.arcgisruntime.geometry.Geometry
import com.esri.arcgisruntime.geometry.GeometryEngine
import com.esri.arcgisruntime.geometry.Polyline
import com.esri.arcgisruntime.geometry.SpatialReference
import com.example.android.mapviewer.data.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

 class ElevationViewModel: ViewModel() {
    private val _response = MutableLiveData<ElevationResponse?>()
     /*val moshi = Moshi.Builder()
         .add(KotlinJsonAdapterFactory())
         .build()*/

    val response: MutableLiveData<ElevationResponse?>
        get() = _response


    private fun getElevationProfile(params: ElevationRequestParam) {
        viewModelScope.launch {
            //_status.value = MarsApiStatus.LOADING
            try {
                _response.value = ElevationApi.retrofitService.getElevationData(params)
               // _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                //_status.value = MarsApiStatus.ERROR
                _response.value = null
            }
        }
    }

   // fun getElevationData(params:ElevationRequestParam)
    fun getElevationData(mFeature:Feature)
    {
        val projectedFeature: Geometry
        if(mFeature.geometry.spatialReference.wkid != 3857) {
            val mercator = SpatialReference.create(3857)
             projectedFeature = GeometryEngine.project(mFeature.geometry,mercator)

        }
        else
            projectedFeature = mFeature.geometry
        val _trailLength = GeometryEngine.length(projectedFeature as Polyline?)
        val trailLength = _trailLength * .000621371
       /* val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.DOWN
        val roundoff_dist = df.format(trailLength)*/
        var maxDistanceSampleSize = 0
        val no_of_vertices = 0
       // val geomJson = mFeature.geometry.toJson() as PathObj
       /* val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()*/
        try {

           // val moshi: Moshi = Moshi.Builder().build()
           // val jsonAdapter: JsonAdapter<BaseDataModel> = moshi.adapter<BaseDataModel>(BaseDataModel::class.java)

            // val moshi:Moshi = Moshi.Builder()
        //    .add(KotlinJsonAdapterFactory())
        //    .build()
         //val moshi = Moshi.Builder().build()



            val jsonAdapter =
               moshi.adapter<BaseDataModel>(BaseDataModel::class.java)//moshi.adapter<Product>(Product::class.java!!)
            val geomJsonObj = mFeature.geometry.toJson()

            val geomJson = jsonAdapter.fromJson(geomJsonObj)

            //val geomJson = geomJsonObj as GeomPath//PathObj
            var no_vertices = 0

            if (geomJson != null) {
                for (k in 0..geomJson.paths.size) {
                    val _path = geomJson.paths[k]
                    no_vertices += _path.size
                }
            }


            if ((no_vertices > 200) && (no_of_vertices < 1024))
                maxDistanceSampleSize = (trailLength / 1500).toInt()

            var _fieldObj = FieldObj(name = "OID", type = "esriFieldTypeObjectID", alias = "OID")
            var fieldsObj = listOf(_fieldObj)
            var _oidObj = OIDObj(OID = 1)
            var _wkidObj = WkidObj(wkid = 102100, latestWkid = 3857)

            var _inputLineFeatures = InputObj(
                fields = fieldsObj, geometryType = "esriGeometryPolyline", attributes = _oidObj,
                sr = _wkidObj
            )

            var elevRequestParam = ElevationRequestParam(
                InputLineFeatures = _inputLineFeatures,
                ProfileIDField = "OID",
                DEMResolution = "FINEST",
                MaximumSampleDistance = maxDistanceSampleSize.toString(),
                MaximumSampleDistanceUits = "Meters",
                returnZ = "true",
                returnM = "true",
                f = "json"
            )



            getElevationProfile(elevRequestParam)
        }
        catch(e: JsonDataException)
        {
            Log.d("Logs", e.toString())
        }
    }
}