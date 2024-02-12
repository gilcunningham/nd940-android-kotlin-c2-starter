package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.entity.AsteroidWrapperDto
import com.udacity.asteroidradar.entity.PictureOfDayDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NeoWsService {

    private val apiKey = BuildConfig.NEO_WS_API_KEY
    //private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val _serviceStatus = MutableSharedFlow<Status>()
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    //private val okHttp = OkHttpClient.Builder()
    //    .addNetworkInterceptor(interceptor)
    //    .build()
    private val moshiRetrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
    //    .client(okHttp)
        .build()
    private val neoWsApi: NeoWsApi by lazy {
        moshiRetrofit.create(NeoWsApi::class.java)
    }
    val serviceStatus = _serviceStatus.asSharedFlow()

    suspend fun fetchAsteroidList(
        formattedStartDate: String,
        formattedEndDate: String
    ): AsteroidWrapperDto? {
        return serviceCall {
            neoWsApi.fetchAsteroidsByDateRange(
                apiKey,
                formattedStartDate,
                formattedEndDate
            )
        }
    }

    suspend fun fetchPictureOfDay(): PictureOfDayDto? {
        return serviceCall {
            neoWsApi.fetchPictureOfDay(apiKey)
        }
    }

    private suspend fun <T : Any> serviceCall(
        serviceDelegate: suspend () -> Response<T>
    ): T? = withContext(Dispatchers.IO) {
        _serviceStatus.emit(Status.LOADING)
       // try {
            serviceDelegate().apply {
                _serviceStatus.emit(if (isSuccessful) Status.DONE else Status.ERROR)
            }.body()
        //} catch (e: Exception) {
        //    e.printStackTrace()
        //    _serviceStatus.emit(Status.ERROR)
        //}
        //null
    }

    /**
    private suspend fun <T> serviceCall2(
        serviceDelegate: suspend () -> Response<T>
    ): T = withContext(Dispatchers.IO) {
        _serviceStatus.emit(Status.LOADING)
        // try {
        //serviceDelegate().apply {
        //    _serviceStatus.emit(if (isSuccessful) Status.DONE else Status.ERROR)
        //}.body()
        //} catch (e: Exception) {
        //    e.printStackTrace()
        //    _serviceStatus.emit(Status.ERROR)
        //}
        //null
    }
    **/



    interface NeoWsApi {
        @GET("/planetary/apod")
        suspend fun fetchPictureOfDay(
            @Query("api_key") apiKey: String
        ): Response<PictureOfDayDto>

        @GET("/neo/rest/v1/feed")
        suspend fun fetchAsteroidsByDateRange(
            @Query("api_key") apiKey: String,
            @Query("start_date") formattedStartDate: String,
            @Query("end_date") formattedEndDate: String,
        ): Response<AsteroidWrapperDto>
    }

    enum class Status { LOADING, ERROR, DONE }
}