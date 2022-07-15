package com.example.android.mapviewer.network

import com.example.android.mapviewer.data.ElevationRequestParam
import com.example.android.mapviewer.data.ElevationResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
// import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://elevation.arcgis.com/arcgis/rest/services/Tools/ElevationSync/GPServer/Profile/execute"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
public val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
/*private val retrofit = Retrofit.Builder()

    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()*/
private val okhttpclient = OkHttpClient()
var eagerClient = okhttpclient.newBuilder()
    .readTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(1, TimeUnit.MINUTES)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(eagerClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface ElevationApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @POST("elevationargs")
    suspend fun getElevationData(@Body requestParams: ElevationRequestParam): ElevationResponse

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ElevationApi {
    val retrofitService : ElevationApiService by lazy { retrofit.create(ElevationApiService::class.java) }
}