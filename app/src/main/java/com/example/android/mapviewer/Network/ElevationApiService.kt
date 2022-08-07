package com.example.android.mapviewer.network

import com.example.android.mapviewer.data.*
//import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
//import okhttp3.Response
//import okhttp3.ResponseBody

//import retrofit2.Call
//import okhttp3.Response
//import okhttp3.ResponseBody
// import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://elevation.arcgis.com"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */

private val okhttpclient = OkHttpClient()
var eagerClient = okhttpclient.newBuilder()
    .readTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(1, TimeUnit.MINUTES)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(eagerClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

/*private val gjson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    .setLenient()
    .create();

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gjson))
    .build()*/

interface ElevationApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    //@Headers("Content-Type: application/json")
    // suspend fun getElevationData(@Body requestParams: RequestBody): Response<ResponseBody>//Response<ElevationResponse>//Response<ResponseBody>//ElevationResponse

    @Multipart
    @POST("/arcgis/rest/services/Tools/ElevationSync/GPServer/Profile/execute")
    fun getElevationData(@PartMap map: HashMap<String, String>): Call<ElevationResponseClass> //ElevationRequestParam

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ElevationApi {
    val retrofitService : ElevationApiService by lazy { retrofit.create(ElevationApiService::class.java) }
}