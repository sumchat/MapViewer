package com.example.android.mapviewer.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esri.arcgisruntime.data.Feature
import com.esri.arcgisruntime.geometry.Geometry
import com.esri.arcgisruntime.geometry.GeometryEngine
import com.esri.arcgisruntime.geometry.Polyline
import com.esri.arcgisruntime.geometry.SpatialReference
import com.example.android.mapviewer.data.*
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ElevationViewModel: ViewModel() {
    private val _response = MutableLiveData<ElevationResponse?>()
     private val moshi = Moshi.Builder()
         .add(KotlinJsonAdapterFactory())
         .build()


    val response: MutableLiveData<ElevationResponse?>
        get() = _response


    private fun getElevationProfile(params: ElevationRequestParam) {
        viewModelScope.launch{
            //_status.value = MarsApiStatus.LOADING
            try {
                val jsonReqAdapter =
                   moshi.adapter<ElevationRequestParam>(ElevationRequestParam::class.java)//moshi.adapter<Product>(Product::class.java!!)
                // val geomJsonObj = mFeature.geometry.toJson()

                val reqJson = jsonReqAdapter.toJson(params)
                //val reqJsonString = reqJson.toString()
                val _requestbody = reqJson.toRequestBody("application/json".toMediaTypeOrNull())
                val response = ElevationApi.retrofitService.getElevationData(_requestbody)
                withContext(Dispatchers.IO){
                   //. if(response != null){
                  if(response.isSuccessful) {
                      val _body = response.body()//.toString()
                     // val jsonResponseAdapter =
                     //     moshi.adapter<ElevationResponse>(ElevationResponse::class.java)


                      Log.d("Logs", "done...")
                  }
                    else
                  {
                      Log.d("Logs", "done")
                  }
                }

                Log.d("Logs", "done")

               // _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                //_status.value = MarsApiStatus.ERROR
                _response.value = null
                Log.d("Logs", e.toString())
            }
        }
    }


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

        try {



            val geomJsonObj = mFeature.geometry.toJson()
            val jsonAdapter =
               moshi.adapter<BaseDataModel>(BaseDataModel::class.java)//moshi.adapter<Product>(Product::class.java!!)
           // val geomJsonObj = mFeature.geometry.toJson()

            val geomJson = jsonAdapter.fromJson(geomJsonObj)

            //val geomJson = geomJsonObj as GeomPath//PathObj
            var no_vertices = 0

            if (geomJson != null) {
                val _to = geomJson.paths.size - 1
                for (k in 0.._to) {
                    val _path = geomJson.paths[k]
                    no_vertices += _path.size
                }
            }


            if ((no_vertices > 200) && (no_of_vertices < 1024))
                maxDistanceSampleSize = (trailLength / 1500).toInt()

            var _fieldObj = FieldsItem(name = "OID", type = "esriFieldTypeObjectID", alias = "OID")
            var fieldsObj = listOf(_fieldObj)
            var _oidObj = Attributes(OID = 1)
            var _wkidObj = SpatialReference(latestWkid = 3857,wkid = 102100)
            var geometryWithoutZ = GeometryEngine.removeZAndM(mFeature.geometry)

           // val geomJsonObj1 = geometryWithoutZ.toJson()

            var _geomObj = geometryWithoutZ.toJson()

           // var _geomObj1 = JSONObject(geometryWithoutZ.toJson())

            val featgeomObject = jsonAdapter.fromJson(_geomObj)
              //JSON.parse(_geomObj)
            var _paths = featgeomObject?.paths//_geomObj1["paths"]
            var _spref = featgeomObject?.spatialReference//_geomObj1["spatialReference"]
            var _geometryItem = GeometryItem(paths= _paths ,
                spatialReference= _spref )

            var _featuresItem = FeaturesItem(geometry=_geometryItem)

            var _inputLineFeatures = InputLineFeaturesObj(
                fields = fieldsObj, geometryType = "esriGeometryPolyline", attributes = _oidObj,
                spatialReference = _wkidObj, features = listOf(_featuresItem)
            )

           // _inputLineFeatures.features = listOf(_geomObj)

            var elevRequestParam = ElevationRequestParam(
                InputLineFeatures = _inputLineFeatures,
                ProfileIDField = "OID",
                DEMResolution = "FINEST",
                MaximumSampleDistanceUnits = "Meters",
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