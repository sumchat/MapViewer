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
//import com.google.gson.Gson
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
//import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.IOException


class ElevationViewModel: ViewModel() {
    private val _response = MutableLiveData<ElevationResponseClass?>()
     private val moshi = Moshi.Builder()
         .add(KotlinJsonAdapterFactory())
         .build()
    val jsoninputAdapter =
        moshi.adapter<InputLineFeaturesObj>(InputLineFeaturesObj::class.java)


    val response: MutableLiveData<ElevationResponseClass?>
        get() = _response

    private fun getElevationProfile(params: ElevationRequestParam) {
        viewModelScope.launch{

            try {
                var paramsbody = HashMap<String,String>()

                val featgeomObject = jsoninputAdapter.toJson(params.InputLineFeatures)

                paramsbody.put("InputLineFeatures",featgeomObject.toString())

                paramsbody.put ("DEMResolution","FINEST")
                paramsbody.put("MaximumSampleDistanceUnits","Meters")
                paramsbody.put("ProfileIDField","OID")
                paramsbody.put("returnFeatureCollection","false")
                paramsbody.put("returnM","true")
                paramsbody.put("returnZ","true")
                paramsbody.put("f","pjson",)

               val call = ElevationApi.retrofitService.getElevationData(paramsbody)

                  call.enqueue(object : Callback<ElevationResponseClass> {

                      override fun onResponse(
                          call: Call<ElevationResponseClass>,
                          response: Response<ElevationResponseClass>
                      ) {
                          Log.d("Logs", "done...")
                          _response.value = response.body()
                      }

                      override fun onFailure(call: Call<ElevationResponseClass>, t: Throwable) {
                          Log.d("Logs", "failed...")
                      }
                  })


                // _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                //_status.value = MarsApiStatus.ERROR
                _response.value = null
                Log.d("Logs", e.toString())
            }
        }
    }


  /* private fun getElevationProfile1(params: InputDataJsonInp)
   {
       viewModelScope.launch{

           try {
               val gson = Gson()
               val jsonObject = gson.toJson(params)
               val json = (jsonObject.toString())

               val payload = json

               val requestBody =  MultipartBody.Builder()
                   .setType(MultipartBody.FORM)
                   .addFormDataPart("InputLineFeatures", "{\"attributes\":{\"OID\":1},\n" +
                           "\"displayFieldName\":\"\",\n" +
                           "\"exceededTransferLimit\":false,\n" +
                           "\"features\":[{\"geometry\":{\"paths\":[[[-1.30468194933E7,4036574.271499999],[-1.30468239164E7,4036866.195799999],\n" +
                           "[-1.30466204541E7,4036923.6960000023]]],\"spatialReference\":{\"latestWkid\":3857,\"wkid\":102100}}}],\n" +
                           "\"fields\":[\n" +
                           "{\"alias\":\"OID\",\n" +
                           "\"name\":\"OID\",\n" +
                           "\"type\":\"esriFieldTypeObjectID\"}],\n" +
                           "\"geometryType\":\"esriGeometryPolyline\",\n" +
                           "\"spatialReference\":{\"latestWkid\":3857,\"wkid\":102100}}")

                   .addFormDataPart ("DEMResolution","FINEST")
                   .addFormDataPart("MaximumSampleDistanceUnits","Meters")
                   .addFormDataPart("ProfileIDField","OID")
                   .addFormDataPart("returnFeatureCollection","false")
                   .addFormDataPart("returnM","true")
                   .addFormDataPart("returnZ","true")
                   .addFormDataPart("f","pjson",)
                   .build()


             /*  val formBody = FormBody.Builder()
                   .add("InputLineFeatures", "{\"attributes\":{\"OID\":1},\n" +
                           "\"displayFieldName\":\"\",\n" +
                           "\"exceededTransferLimit\":false,\n" +
                           "\"features\":[{\"geometry\":{\"paths\":[[[-1.30468194933E7,4036574.271499999],[-1.30468239164E7,4036866.195799999],\n" +
                           "[-1.30466204541E7,4036923.6960000023]]],\"spatialReference\":{\"latestWkid\":3857,\"wkid\":102100}}}],\n" +
                           "\"fields\":[\n" +
                           "{\"alias\":\"OID\",\n" +
                           "\"name\":\"OID\",\n" +
                           "\"type\":\"esriFieldTypeObjectID\"}],\n" +
                           "\"geometryType\":\"esriGeometryPolyline\",\n" +
                           "\"spatialReference\":{\"latestWkid\":3857,\"wkid\":102100}}")

                   .add ("DEMResolution","FINEST")
                   .add("MaximumSampleDistanceUnits","Meters")
                   .add("ProfileIDField","OID")
                   .add("returnFeatureCollection","false")
                   .add("returnM","true")
                   .add("returnZ","true")
                   .add("f","pjson",)
                   .build()*/

               val request = Request.Builder()
                   .url("https://elevation.arcgis.com/arcgis/rest/services/Tools/ElevationSync/GPServer/Profile/execute")
                   .post(requestBody)
                   .build()

               val okHttpClient = OkHttpClient()

               okHttpClient.newCall(request).enqueue(object : Callback {
                   override fun onFailure(call: Call, e: IOException) {
                       Log.d("Logs","error")
                       // Handle this
                   }

                   override fun onResponse(call: Call, response: Response) {
                       Log.d("Logs","error")
                       // Handle this
                   }
               })


           } catch (e: Exception) {
               //_status.value = MarsApiStatus.ERROR
               _response.value = null
               Log.d("Logs", e.toString())
           }
       }
   }
*/
 /*   fun prepareInputData(mFeature:Feature):InputDataJsonInp
    {
        var _fieldObj = FieldsItemInp(name = "OID", type = "esriFieldTypeObjectID", alias = "OID")
        var fieldsObj = listOf(_fieldObj)
        var _oidObj = AttributesInp(oid = 1)
        var _wkidObj = SpatialReferenceInp(latestWkid = 3857,wkid = 102100)
        var geometryWithoutZ = GeometryEngine.removeZAndM(mFeature.geometry)

        var _geomObj = geometryWithoutZ.toJson()
        val jsonAdapter =
            moshi.adapter<BaseDataModel>(BaseDataModel::class.java)


        val featgeomObject = jsonAdapter.fromJson(_geomObj)

        var _paths = featgeomObject?.paths
        var _spref = featgeomObject?.spatialReference
        var sprefInp = _spref?.latestWkid?.let { _spref.wkid?.let { it1 -> SpatialReferenceInp(latestWkid = it,wkid = it1) } }
        var _geometryItem = sprefInp?.let {
            GeometryInp(paths= _paths ,
                spatialReference= it
            )
        }

        var _featuresItem = _geometryItem?.let { FeaturesItemInp(geometry= it) }

        var _inputLineFeatures = InputLineFeaturesInp(
            fields = fieldsObj, geometryType = "esriGeometryPolyline", attributes = _oidObj,
            spatialReference = _wkidObj, features = listOf(_featuresItem)
        )

        // _inputLineFeatures.features = listOf(_geomObj)

        var elevRequestParam = InputDataJsonInp(
            inputLineFeatures = _inputLineFeatures,
            profileIDField = "OID",
            dEMResolution = "FINEST",
            maximumSampleDistanceUnits = "Meters",
            returnZ = true,
            returnM = true,
            f = "pjson"
        )

      return elevRequestParam

    }

*/

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


            var _geomObj = geometryWithoutZ.toJson()


            val featgeomObject = jsonAdapter.fromJson(_geomObj)

            var _paths = featgeomObject?.paths
            var _spref = featgeomObject?.spatialReference
            var _geometryItem = GeometryItem(paths= _paths ,
                spatialReference= _spref )

            var _featuresItem = FeaturesItem(geometry=_geometryItem)

            var _inputLineFeatures = InputLineFeaturesObj(
                fields = fieldsObj, geometryType = "esriGeometryPolyline", attributes = _oidObj,
                spatialReference = _wkidObj, features = listOf(_featuresItem)
            )



            var elevRequestParam = ElevationRequestParam(
                InputLineFeatures = _inputLineFeatures,
                ProfileIDField = "OID",
                DEMResolution = "FINEST",
                MaximumSampleDistanceUnits = "Meters",
                returnZ = "true",
                returnM = "true",
                f = "json"
            )

          // val elevRequestParam = prepareInputData(mFeature)

            getElevationProfile(elevRequestParam)
            //getElevationDataOhttp(elevRequestParam)
        }
        catch(e: JsonDataException)
        {
            Log.d("Logs", e.toString())
        }
    }




}